package com.viettel.core.repository;

import com.viettel.commons.repository.InsertUpdateRepository;
import com.viettel.core.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company>, InsertUpdateRepository<Company> {

}
