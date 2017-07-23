package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }
    //для первичного наполнения
    private void save(Meal meal){
        this.save(meal,AuthorizedUser.id());
    }

    @Override
    public Meal save(Meal meal,int userId) {
        //если новая или принадлежит пользователю - сохраняем, есл не пользователя - возвращаем null
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }else if (meal.getUserId()!=userId) {
            return null;
        }
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        //удаляем еду, если удаленная еда не принадлежит пользователю - кладем ее обратно.
        Meal meal = repository.remove(id);
        if (meal==null) {
            return false;
        }else if (meal.getUserId()!=userId ){
            repository.put(meal.getId(),meal);
            return false;
        }
        return true;
    }

    @Override
    public Meal get(int id,int userId) {
        Meal meal = repository.get(id);
        //если еда есть и принадлежит пользователю  - возвращаем, иначе null
        return meal!=null&&meal.getUserId()==userId?meal:null;
    }

    @Override
    public Collection<Meal> getAll(int userId, LocalDate start,LocalDate end) {
        return repository.values().stream()
                .filter(a->(a.getUserId()==userId))
                .filter(a->DateTimeUtil.isBetween(a.getDate(),start,end))
                .collect(Collectors.toList());
    }
}

