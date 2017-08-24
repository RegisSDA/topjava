package ru.javawebinar.topjava.service.jdbc;

import org.hibernate.boot.jaxb.SourceType;
import org.junit.Test;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;


import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @Override
    @Test(expected = DuplicateKeyException.class)
    public void testDuplicateMailCreate() throws DuplicateKeyException{
 //       throw new DuplicateKeyException("sdfs");
        //      //  thrown.expect(DataAccessException.class);
            try {
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER));
                double res = 0;
                for (int i = 0; i < 10000; i++) {
                    res += Math.PI * Math.E % i;
                }
            }catch (final Exception e){
                System.out.println("Что-то поймано");
            }

    }

}