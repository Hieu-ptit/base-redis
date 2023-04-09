package com.viettel.core.redis.publisher;

import com.dslplatform.json.JsonReader;
import com.viettel.commons.util.Json;
import com.viettel.core.model.response.CompanyResponse;
import com.viettel.core.redis.caching.RedisCaching;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Log4j2
public class JedisSubscribe {
    @Autowired
    private JedisPool jedisPool;
    private final JsonReader.ReadObject<CompanyResponse> objectWriteObject = Json.findReader(CompanyResponse.class);
    @Value("${redis.chanel}")
    private String chanel;

    @PostConstruct
    public void subscribeToChannel() {
        Thread subscriptionThread = new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                JedisPubSub jedisPubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        var result = Json.decode(message.getBytes(StandardCharsets.UTF_8), objectWriteObject);
                        log.info("chanel = {}, message = {}", channel, result);
                    }
                };
                jedis.subscribe(jedisPubSub, chanel);
            }
        });
        subscriptionThread.start();
    }

    @PreDestroy
    public void closeConnection() {
        jedisPool.close();
    }
}
