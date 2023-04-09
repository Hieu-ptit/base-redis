package com.viettel.core.controller;

import com.viettel.commons.model.response.Response;
import com.viettel.core.model.request.CompanyRequest;
import com.viettel.core.model.response.CompanyResponse;
import com.viettel.core.service.CompanyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
@Validated
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping
    public Response<Void> create(@Valid @RequestBody CompanyRequest request) {
        companyService.create(request);
        return Response.ofSucceeded();
    }

    @GetMapping("/{id}")
    public Response<CompanyResponse> getById(@NonNull @PathVariable ("id") Long id){
        return Response.ofSucceeded(companyService.getById(id));
    }
}
