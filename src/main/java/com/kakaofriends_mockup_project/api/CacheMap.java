package com.kakaofriends_mockup_project.api;

import com.kakaofriends_mockup_project.utils.CacheUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Objects.isNull;

public class CacheMap extends HashMap<String, Object> {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public Object getValue(String key) {
        return CacheUtils.cacheMap.get(key);
    }

    public void set(String key, Object value) {
        writeLock.lock();
        try {
            if(isNull(getValue(key))) {
                CacheUtils.cacheMap.put(key, value);    // critical section
            }
        } finally {
            writeLock.unlock();
        }
    }

}
