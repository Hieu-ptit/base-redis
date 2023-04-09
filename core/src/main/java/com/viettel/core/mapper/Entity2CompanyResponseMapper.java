package com.viettel.core.mapper;

import com.viettel.commons.mapper.BeanMapper;
import com.viettel.core.model.entity.Company;
import com.viettel.core.model.response.CompanyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface Entity2CompanyResponseMapper extends BeanMapper<Company, CompanyResponse> {
    Entity2CompanyResponseMapper INSTANCE = Mappers.getMapper(Entity2CompanyResponseMapper.class);

}
