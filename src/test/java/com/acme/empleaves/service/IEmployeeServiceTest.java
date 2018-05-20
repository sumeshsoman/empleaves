package com.acme.empleaves.service;

import com.acme.empleaves.model.Employee;
import com.acme.empleaves.model.Leave;
import com.acme.empleaves.model.LeaveState;
import com.acme.empleaves.model.LeaveType;
import com.acme.empleaves.model.WorkingHours;
import com.acme.empleaves.repo.EmployeeRepository;
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
import java.util.HashSet;
import java.util.Set;

import static java.time.temporal.ChronoUnit.HOURS;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Transactional
public class IEmployeeServiceTest {

  @Autowired private EmployeeRepository employeeRepository;

  @Before
  public void setUp() {
    Employee employee = new Employee("Scott", "Lewis", "Manager");
    LocalDate date = LocalDate.now();

    LocalDate tues = date.with(DayOfWeek.TUESDAY);
    LocalDate wednes = date.with(DayOfWeek.WEDNESDAY);
    LocalDate mon = date.with(DayOfWeek.MONDAY);
    LocalDate thu = date.with(DayOfWeek.THURSDAY);
    LocalDate fri = date.with(DayOfWeek.FRIDAY);

    Leave leave1 = new Leave(LeaveState.APPLIED, LeaveType.ANNUAL, tues.toEpochDay());
    Leave leave2 = new Leave(LeaveState.APPLIED, LeaveType.ANNUAL, wednes.toEpochDay());
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
  }

  @Test
  public void findEmployeeWorkingHours() {
    Employee employee = employeeRepository.findAll().iterator().next();
    Assert.assertNotNull(employee);

    Set<WorkingHours> workingHoursSet = employee.getMyWorkingHours();
    Assert.assertEquals(3, workingHoursSet.size());
    workingHoursSet.stream().forEach(e -> Assert.assertEquals(8 * 60 * 60, e.getLogOffTime() - e.getLogOnTime()));
  }
}
