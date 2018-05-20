package com.acme.empleaves.repo;

import com.acme.empleaves.model.Leave;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends CrudRepository<Leave, Long> {
  List<Leave> findByOnDateMillisIsLessThanEqualAndEmployee_IdOrderByOnDateMillis(
      Long onDateMillis, Long id);
}
