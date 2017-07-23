package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id)!=null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        return repository.put(user.getId(),user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        //сортировка по имени и упаковка в List
        return repository.values().stream().sorted((a,b)->(a.getName().compareTo(b.getName()))).collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        if (email == null) {
            return null;
        }
        log.info("getByEmail {}", email);
        //поиск по e-mail и возвращение любого найденного, при отсутствии пользователя с заданной почтой возвращаем null;
        Optional<User> user = repository.values().stream().filter(a->email.equals(a.getEmail())).findAny();
        return user.isPresent()?user.get():null;
    }
}
