package com.acme.empleaves.service;

import com.acme.empleaves.model.WorkingHours;

import java.util.List;

public interface IEmployeeService {
  List<WorkingHours> findEmployeeWorkingHours(Long id, Long startTrackTime, Long endTrackTime);
}
