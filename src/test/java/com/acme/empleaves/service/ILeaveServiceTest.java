package com.acme.empleaves.service;

import com.acme.empleaves.model.Employee;
import com.acme.empleaves.model.Leave;
import com.acme.empleaves.model.LeaveBenefits;
import com.acme.empleaves.model.LeaveState;
import com.acme.empleaves.model.LeaveType;
import com.acme.empleaves.model.WorkingHours;
import com.acme.empleaves.repo.EmployeeRepository;
import com.acme.empleaves.repo.LeaveBenefitsRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.time.temporal.ChronoUnit.HOURS;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
public class ILeaveServiceTest {

  @Autowired private EmployeeRepository employeeRepository;
  @Autowired private LeaveBenefitsRepository leaveBenefitsRepository;

  @Autowired private ILeaveService leaveService;

  @Before
  public void setUp() {
    Employee employee = new Employee("Scott", "Lewis", "Manager");
    LocalDate date = LocalDate.now();

    LocalDate tues = date.with(DayOfWeek.TUESDAY);
    LocalDate wednes = date.with(DayOfWeek.WEDNESDAY);
    LocalDate mon = date.with(DayOfWeek.MONDAY);
    LocalDate thu = date.with(DayOfWeek.THURSDAY);
    LocalDate fri = date.with(DayOfWeek.FRIDAY);

    Leave leave1 = new Leave(LeaveState.APPLIED, LeaveType.ANNUAL, tues.atStartOfDay().toEpochSecond(ZoneOffset.UTC));
    Leave leave2 = new Leave(LeaveState.APPLIED, LeaveType.ANNUAL, wednes.atStartOfDay().toEpochSecond(ZoneOffset.UTC));
    Set<Leave> leaveSet = new HashSet<>(Arrays.asList(leave1, leave2));
    WorkingHours workingHours1 =
        new WorkingHours(
            mon.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
            mon.atStartOfDay().plus(8, HOURS).toEpochSecond(ZoneOffset.UTC),
            Long.valueOf(8));
    WorkingHours workingHours2 =
        new WorkingHours(
            thu.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
            thu.atStartOfDay().plus(8, HOURS).toEpochSecond(ZoneOffset.UTC),
            Long.valueOf(8));
    WorkingHours workingHours3 =
        new WorkingHours(
            fri.atStartOfDay().toEpochSecond(ZoneOffset.UTC),
            fri.atStartOfDay().plus(8, HOURS).toEpochSecond(ZoneOffset.UTC),
            Long.valueOf(8));
    Set<WorkingHours> workingHoursSet =
        new HashSet<>(Arrays.asList(workingHours1, workingHours2, workingHours3));

    employee.setMyAppliedLeaves(leaveSet);
    employee.setMyWorkingHours(workingHoursSet);

    employeeRepository.save(employee);

    Map<String, Integer> benefitsMap = new HashMap();
    benefitsMap.put(LeaveType.ANNUAL.toString(), 20);
    benefitsMap.put(LeaveType.SICK.toString(), 10);
    benefitsMap.put(LeaveType.TRAINING.toString(), 2);

    LeaveBenefits benefits = new LeaveBenefits();
    benefits.setDesignation("Manager");
    benefits.setMaxLeaveEligibilityMap(benefitsMap);
    leaveBenefitsRepository.save(benefits);
  }

  @Test
  public void findLeavesByEmployee() {
    Employee employee = employeeRepository.findAll().iterator().next();
    Assert.assertNotNull(employee);
    Assert.assertEquals(2, employee.getMyAppliedLeaves());
    List<Leave> leaveList = leaveService.findLeavesByEmployee(employee.getId());
    Assert.assertEquals(2, leaveList.size());
  }

  @Test
  public void findEmployeeLeaveBreakUp() {
    Employee employee = employeeRepository.findAll().iterator().next();
    Assert.assertNotNull(employee);

    LocalDate date = LocalDate.now();

    LocalDate mon = date.with(DayOfWeek.MONDAY);
    Map<String, List<Leave>> listMap =
        leaveService.findEmployeeLeaveBreakUp(
            employee.getId(), mon.atStartOfDay().toEpochSecond(ZoneOffset.UTC));

    Assert.assertEquals(2, listMap.get("Next 7 Days").size());
    Assert.assertEquals(2, listMap.get("This Month").size());
  }

  @Test
  public void findLeaveBalanceByEmployee() {}
}
