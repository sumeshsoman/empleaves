package com.acme.empleaves.repo;

import com.acme.empleaves.model.LeaveBenefits;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveBenefitsRepository extends CrudRepository<LeaveBenefits, Long> {
    LeaveBenefits findByDesignation(String designation);
}
