package com.viettel.core.redis.caching;

public interface RedisCaching {
    String findData(int db, String key);
    boolean saveData(int db, String key, String data, Long seconds);
    void deleteData(int db, String key);
}
