package com.acme.empleaves.repo;

import com.acme.empleaves.model.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

  Employee findByLastNameAndFirstName(String lastName, String firstName);
}
