package ru.otus.hw11;

import lombok.Getter;
import ru.otus.hw11.data.UserDataSet;
import ru.otus.hw11.service.HibernateServiceImpl;

import static ru.otus.hw11.Constants.CACHE_LIFE_TIME;
import static ru.otus.hw11.Constants.CACHE_SIZE;

public class HibernateCacheService extends HibernateServiceImpl {
    @Getter private final CacheEngine<Long, UserDataSet> cache = CacheEngineFactory.create(CACHE_SIZE, CACHE_LIFE_TIME, 0);

    @Override
    public UserDataSet load(long id) {
        CacheElem<Long, UserDataSet> cached = cache.get(id);
        if (cached != null) {
            return cached.getValue();
        }
        UserDataSet user = super.load(id);
        if (user != null) {
            cache.put(new CacheElem<>(id, user));
        }
        return user;
    }

    @Override
    public void save(UserDataSet dataSet) {
        super.save(dataSet);
        cache.put(new CacheElem<>(dataSet.getId(), dataSet));
    }
}