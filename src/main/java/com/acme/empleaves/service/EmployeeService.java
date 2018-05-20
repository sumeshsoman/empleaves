package com.acme.empleaves.service;

import com.acme.empleaves.model.Employee;
import com.acme.empleaves.model.WorkingHours;
import com.acme.empleaves.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements IEmployeeService {

  @Autowired private EmployeeRepository employeeRepository;

  @Override
  public List<WorkingHours> findEmployeeWorkingHours(
      Long id, Long startTrackTime, Long endTrackTime) {
    Optional<Employee> employeeOptional = employeeRepository.findById(id);
    List<WorkingHours> workingHours = new ArrayList<>();
    if (employeeOptional.isPresent()) {
      Employee employee = employeeOptional.get();
      workingHours =
          employee
              .getMyWorkingHours()
              .stream()
              .sorted(Comparator.comparingLong(WorkingHours::getLogOnTime))
              .filter(
                  wh -> (wh.getLogOnTime() >= startTrackTime && wh.getLogOnTime() <= endTrackTime))
              .collect(Collectors.toList());
    }

    return workingHours;
  }
}
