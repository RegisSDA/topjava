package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private String SQLInsert = "insert into user_roles (user_id, role) VALUES (?,?)";
    private String SQLDelete = "delete from user_roles where user_id=? and role =?";

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Override
    public User save(User user) {
        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        TransactionStatus txStatus = dataSourceTransactionManager.getTransaction(txDef);
        try {
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

            if (user.isNew()) {
                Number newKey = insertUser.executeAndReturnKey(parameterSource);
                user.setId(newKey.intValue());
                butch(SQLInsert,new ArrayList<>(user.getRoles()),user.getId());
            } else {
                namedParameterJdbcTemplate.update(
                        "UPDATE users SET name=:name, email=:email, password=:password, " +
                                "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
                Set<String> rolesSet = new HashSet<>(jdbcTemplate.queryForList("select role from user_roles where user_id=?",String.class,user.getId()));
                List<Role> toInsert = new ArrayList<>();

                user.getRoles().forEach(a->{
                    if (!rolesSet.contains(a.toString())) {
                        toInsert.add(a);
                    }else{
                        rolesSet.remove(a.toString());
                    }
                });
                butch(SQLInsert,toInsert,user.getId());
                butch(SQLDelete,rolesSet.stream().map(Role::valueOf).collect(Collectors.toList()), user.getId());

            }
            dataSourceTransactionManager.commit(txStatus);
            return user;
        }catch(Exception e){
            e.printStackTrace();
            dataSourceTransactionManager.rollback(txStatus);
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if(user==null){
            return null;
        }
        List<String> rolesList = jdbcTemplate.queryForList("select role from user_roles where user_id=?",String.class,user.getId());
        Set<Role> roles = new HashSet<>();
        rolesList.forEach(a->roles.add(Role.valueOf(a)));
        user.setRoles(roles);
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if(user==null){
            return null;
        }
        List<String> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?",String.class,user.getId());
        Set<Role> setOfRoles = new HashSet<>();
        roles.forEach(r->setOfRoles.add(Role.valueOf(r)));
        user.setRoles(setOfRoles);
        return user;
    }

    @Override
    public List<User> getAll() {
        List<Map<String,Object>> userToRoleTable=jdbcTemplate.queryForList("SELECT role, user_id FROM user_roles");
        Map<Integer,Set<Role>> idToRolesMap = new HashMap<>();
        userToRoleTable.forEach(a->idToRolesMap.computeIfAbsent((Integer)a.get("user_id"),HashSet<Role>::new).add(Role.valueOf((String)a.get("role"))));
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        users.forEach(u->u.setRoles(idToRolesMap.get(u.getId())));
        return users;
    }

    private void butch(String sql, List<Role> list,int userId){
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Role role = list.get(i);
                ps.setInt(1,userId);
                ps.setString(2,role.toString());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

}
