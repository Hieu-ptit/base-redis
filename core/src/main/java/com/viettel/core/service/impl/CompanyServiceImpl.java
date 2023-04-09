package com.viettel.core.service.impl;

import com.dslplatform.json.JsonReader;
import com.dslplatform.json.JsonWriter;
import com.viettel.commons.util.Json;
import com.viettel.core.mapper.CompanyMapperEntity2;
import com.viettel.core.mapper.Entity2CompanyResponseMapper;
import com.viettel.core.model.entity.Company;
import com.viettel.core.model.request.CompanyRequest;
import com.viettel.core.model.response.CompanyResponse;
import com.viettel.core.redis.caching.RedisCaching;
import com.viettel.core.redis.notifier.JedisNotifier;
import com.viettel.core.repository.CompanyRepository;
import com.viettel.core.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final RedisCaching redisCaching;
    private final JsonWriter.WriteObject<CompanyResponse> COMPANY_RESPONSE_READ_OBJECT = Json.findWriter(CompanyResponse.class);
    private final JsonReader.ReadObject<CompanyResponse> objectWriteObject = Json.findReader(CompanyResponse.class);
    private final JedisNotifier jedisNotifier;
    @Value("${redis.chanel}")
    private String chanel;

    @Override
    public void create(CompanyRequest request) {
        var now = OffsetDateTime.now();
        var company = CompanyMapperEntity2.INSTANCE.map(request)
                .setCreatedAt(now).setUpdatedAt(now);
        companyRepository.insert(company);
        var companyResponse = Entity2CompanyResponseMapper.INSTANCE.map(company);
        jedisNotifier.publish(chanel, Json.encodeToString(companyResponse, COMPANY_RESPONSE_READ_OBJECT));
        redisCaching.saveData(1, companyResponse.getId().toString(), Json.encodeToString(companyResponse, COMPANY_RESPONSE_READ_OBJECT), null);
//        var result = Json.decode(redisCaching.findData(1, companyResponse.getId().toString()).getBytes(StandardCharsets.UTF_8), objectWriteObject);
//        log.info("company {}", result);
    }

    @Override
    public CompanyResponse getById(Long id) {
        return Json.decode(redisCaching.findData(1, id.toString()).getBytes(StandardCharsets.UTF_8), objectWriteObject);
    }
}
