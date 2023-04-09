package com.viettel.core.redis.caching.impl;

import com.viettel.core.redis.caching.RedisCaching;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

@Service
@RequiredArgsConstructor
public class RedisCachingImpl implements RedisCaching {
    private final JedisPool jedisPool;
    @Value("${redis.connect-timeout}")
    private Long timeout;

    @Override
    public String findData(int db, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(db);
            if (jedis.exists(key)) {
                return jedis.get(key);
            }
        }
        return null;
    }

    @Override
    public boolean saveData(int db, String key, String data, Long seconds) {
        if (seconds == null)
            seconds = timeout;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(db);
            String status = jedis.set(key, data, SetParams.setParams().ex(seconds));
            return !StringUtils.isEmpty(status);
        }
    }

    @Override
    public void deleteData(int db, String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(db);
            if (jedis.exists(key))
                jedis.del(key);
        }
    }
}
