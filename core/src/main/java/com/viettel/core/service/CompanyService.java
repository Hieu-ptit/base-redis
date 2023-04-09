package com.viettel.core.service;

import com.viettel.core.model.request.CompanyRequest;
import com.viettel.core.model.response.CompanyResponse;

public interface CompanyService {
    void create(CompanyRequest request);

    CompanyResponse getById(Long id);
}
