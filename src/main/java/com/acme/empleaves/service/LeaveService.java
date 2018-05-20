package com.acme.empleaves.service;

import com.acme.empleaves.model.Employee;
import com.acme.empleaves.model.Leave;
import com.acme.empleaves.model.LeaveBalance;
import com.acme.empleaves.model.LeaveBenefits;
import com.acme.empleaves.repo.EmployeeRepository;
import com.acme.empleaves.repo.LeaveBenefitsRepository;
import com.acme.empleaves.repo.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoField.EPOCH_DAY;

@Service
public class LeaveService implements ILeaveService {

  @Autowired private EmployeeRepository employeeRepository;
  @Autowired private LeaveRepository leaveRepository;
  @Autowired private LeaveBenefitsRepository leaveBenefitsRepository;

  @Override
  public List<Leave> findLeavesByEmployee(Long employeeId) {
    Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
    if (employeeOptional.isPresent()) {
      Employee employee = employeeOptional.get();
      Set<Leave> empLeaves = employee.getMyAppliedLeaves();
      empLeaves.stream().sorted(Comparator.comparing(Leave::getOnDateMillis));
      return empLeaves.stream().collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  @Override
  public Map<String, List<Leave>> findEmployeeLeaveBreakUp(Long employeeId, Long dateInMillis) {

    Map<String, List<Leave>> mapToReturn = new HashMap<>();
    Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
    if (employeeOptional.isPresent()) {
      List<Leave> leavesOfToday = new ArrayList<>();
      List<Leave> leavesOfNextSevenDays = new ArrayList<>();
      List<Leave> leavesOfThisMonth = new ArrayList<>();
      Employee employee = employeeOptional.get();
      Set<Leave> empLeaves = employee.getMyAppliedLeaves();
      for (Leave leave : empLeaves) {
        if (isLeaveDateToday(leave, dateInMillis)) {
          leavesOfToday.add(leave);
        }
        if (isLeaveDateInNextSevenDays(leave, dateInMillis)) {
          leavesOfNextSevenDays.add(leave);
        }
        if (isLeaveDatePartOfCurrentMonth(leave, dateInMillis)) {
          leavesOfThisMonth.add(leave);
        }
      }
      mapToReturn.put("Today", leavesOfToday);
      mapToReturn.put("Next 7 Days", leavesOfNextSevenDays);
      mapToReturn.put("This Month", leavesOfThisMonth);
    }

    return mapToReturn;
  }

  @Override
  public List<LeaveBalance> findLeaveBalanceByEmployee(Long employeeId) {
    List<LeaveBalance> leaveBalances = new ArrayList<>();
    Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
    if (employeeOptional.isPresent()) {
      Map<String, LeaveBalance> leaveBalanceIntegerMap = new HashMap<>();
      Employee employee = employeeOptional.get();
      Set<Leave> appliedLeaves = employee.getMyAppliedLeaves();
      for(Leave leave: appliedLeaves){
        LeaveBalance balance = leaveBalanceIntegerMap.get(leave.getLeaveType().toString());
        if(null == balance){
          balance = new LeaveBalance(leave.getLeaveType());
        }
        balance.addToTaken(1);
        leaveBalanceIntegerMap.put(leave.getLeaveType().toString(), balance);
      }

      LeaveBenefits benefits = leaveBenefitsRepository.findByDesignation(employee.getDesignation());
      Map<String, Integer> leaveBenefitsMap = benefits.getMaxLeaveEligibilityMap();
      for(Map.Entry<String, Integer> entry: leaveBenefitsMap.entrySet()){
        LeaveBalance balance = leaveBalanceIntegerMap.get(entry.getKey());
        balance.setEarned(new Double(entry.getValue()));
        leaveBalanceIntegerMap.put(entry.getKey(), balance);
      }

      leaveBalances = new ArrayList<>(leaveBalanceIntegerMap.values());
    }

    return leaveBalances;
  }

  private boolean isLeaveDateToday(Leave leave, Long dateInMillis) {
    boolean value =
        (leave.getOnDateMillis().longValue() == dateInMillis.longValue()) ? true : false;
    return value;
  }

  private boolean isLeaveDateInNextSevenDays(Leave leave, Long dateInMillis) {
    LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dateInMillis, 0,ZoneOffset.UTC);
    LocalDate dateProvided = localDateTime.toLocalDate();
    LocalDate afterSevenDays = dateProvided.plusWeeks(1);
    if (leave.getOnDateMillis().longValue() >= dateInMillis
        && leave.getOnDateMillis().longValue() <= afterSevenDays.atStartOfDay().toEpochSecond(ZoneOffset.UTC)) return true;

    return false;
  }

  private boolean isLeaveDatePartOfCurrentMonth(Leave leave, Long dateInMillis) {
    LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(dateInMillis, 0,ZoneOffset.UTC);
    LocalDate dateProvided = localDateTime.toLocalDate();
    Month monthProvided = dateProvided.getMonth();

    LocalDateTime leaveLocalDateTime = LocalDateTime.ofEpochSecond(leave.getOnDateMillis(), 0,ZoneOffset.UTC);
    LocalDate leaveDate = leaveLocalDateTime.toLocalDate();

    if (leaveDate.getMonth().equals(monthProvided)) {
      return true;
    }

    return false;
  }

}
