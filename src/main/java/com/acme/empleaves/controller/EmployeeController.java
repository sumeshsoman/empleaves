package com.acme.empleaves.controller;

import com.acme.empleaves.model.Employee;
import com.acme.empleaves.model.Leave;
import com.acme.empleaves.model.LeaveBalance;
import com.acme.empleaves.model.WorkingHours;
import com.acme.empleaves.repo.EmployeeRepository;
import com.acme.empleaves.service.IEmployeeService;
import com.acme.empleaves.service.ILeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class EmployeeController {

  public static final String employeeURI = "/acme/v1/employee";
  public static final String manage_leave_employeeURI = "/acme/v1/manage_leave/employee";
  public static final String absent_employeeURI = "/acme/v1/absent/employee";
  public static final String leave_status_employeeURI = "/acme/v1/leavestatus/employee";
  public static final String working_hours_employeeURI = "/acme/v1/working_hour/employee";
  public static final String leave_balance_employeeURI = "/acme/v1/leavebalance/employee";


  @Autowired private EmployeeRepository employeeRepository;
  @Autowired private ILeaveService leaveService;
  @Autowired private IEmployeeService employeeService;

  @RequestMapping(
    value = employeeURI,
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET
  )
  public ResponseEntity<List<Employee>> getEmployee() {
    List<Employee> employees = new ArrayList<>();
    HttpStatus status = HttpStatus.OK;
    employeeRepository.findAll().forEach(e -> employees.add(e));
    return new ResponseEntity<>(employees, status);
  }

  @RequestMapping(
    value = employeeURI + "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET
  )
  public ResponseEntity<?> getEmployeeById(@PathVariable("id") String id) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    Optional<Employee> employeeOptional = employeeRepository.findById(Long.valueOf(id));
    if (employeeOptional.isPresent()) {
      status = HttpStatus.OK;
      return new ResponseEntity<>(employeeOptional.get(), status);
    }
    return new ResponseEntity<>(status);
  }

  @RequestMapping(
    value = employeeURI,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST
  )
  public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
      HttpStatus status = HttpStatus.CREATED;
    Employee emp = employeeRepository.findByLastNameAndFirstName(employee.getLastName(), employee.getFirstName());
    if (null == emp) {
      employeeRepository.save(employee);
    }else {
        status = HttpStatus.CONFLICT;
    }
    return new ResponseEntity<>(employee, status);
  }

    @RequestMapping(
            value = manage_leave_employeeURI + "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public ResponseEntity<List<Leave>> getEmployeeLeavesForManagement(@PathVariable("id") String id) {
        List<Leave> leaveList;
        HttpStatus status = HttpStatus.OK;
        leaveList = leaveService.findLeavesByEmployee(Long.valueOf(id));
        return new ResponseEntity<>(leaveList, status);
    }

    @RequestMapping(
            value = absent_employeeURI + "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public ResponseEntity<Map<String, Integer>> getEmployeeAbsentDetails(@PathVariable("id") String id) {
        HttpStatus status = HttpStatus.OK;
        LocalDate date = LocalDate.now();
        Map<String, List<Leave>> stringListMap = leaveService.findEmployeeLeaveBreakUp(Long.valueOf(id), date.toEpochDay());
        Map<String, Integer> resultMap = new HashMap<>();
        for(Map.Entry<String, List<Leave>> entry : stringListMap.entrySet()){
            resultMap.put(entry.getKey(), entry.getValue().size());
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @RequestMapping(
            value = working_hours_employeeURI + "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public ResponseEntity<List<WorkingHours>> getEmployeeWorkingHours(@PathVariable("id") String id) {
        HttpStatus status = HttpStatus.OK;
        LocalDate date = LocalDate.now();

        LocalDate monday = date.with(DayOfWeek.MONDAY);
        LocalDate saturday = date.with(DayOfWeek.SATURDAY);

        List<WorkingHours> workingHours = employeeService.findEmployeeWorkingHours(Long.valueOf(id), monday.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
                saturday.atStartOfDay().toEpochSecond(ZoneOffset.UTC));
        return new ResponseEntity<>(workingHours, status);
    }

    @RequestMapping(
            value = leave_balance_employeeURI + "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET
    )
    public ResponseEntity<List<LeaveBalance>> getEmployeeLeaveBalance(@PathVariable("id") String id) {
        HttpStatus status = HttpStatus.OK;
        LocalDate date = LocalDate.now();

        List<LeaveBalance> leaveBalances = leaveService.findLeaveBalanceByEmployee(Long.valueOf(id));
        return new ResponseEntity<>(leaveBalances, status);
    }
}
