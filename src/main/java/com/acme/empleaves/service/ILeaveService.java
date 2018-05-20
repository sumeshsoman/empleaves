package com.acme.empleaves.service;

import com.acme.empleaves.model.Leave;
import com.acme.empleaves.model.LeaveBalance;

import java.util.List;
import java.util.Map;

public interface ILeaveService {
  List<Leave> findLeavesByEmployee(Long employeeId);

  Map<String, List<Leave>> findEmployeeLeaveBreakUp(Long employeeId, Long dateInMillis);

  List<LeaveBalance> findLeaveBalanceByEmployee(Long employeeId);
}
