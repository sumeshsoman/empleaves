package com.acme.empleaves.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Leave implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private LeaveState leaveState;
  private LeaveType leaveType;
  private Long onDateMillis;

  @OneToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;

  public Leave() {}

  public Leave(LeaveState leaveState, LeaveType leaveType, Long onDateMillis) {
    this.leaveState = leaveState;
    this.leaveType = leaveType;
    this.onDateMillis = onDateMillis;
  }

  public Long getId() {
    return id;
  }

  public LeaveState getLeaveState() {
    return leaveState;
  }

  public void setLeaveState(LeaveState leaveState) {
    this.leaveState = leaveState;
  }

  public LeaveType getLeaveType() {
    return leaveType;
  }

  public void setLeaveType(LeaveType leaveType) {
    this.leaveType = leaveType;
  }

  public Long getOnDateMillis() {
    return onDateMillis;
  }

  public void setOnDateMillis(Long onDateMillis) {
    this.onDateMillis = onDateMillis;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  @Override
  public String toString() {
    return "Leave{"
        + "leaveState="
        + leaveState
        + ", leaveType="
        + leaveType
        + ", onDateMillis="
        + onDateMillis
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Leave leave = (Leave) o;
    return Objects.equals(onDateMillis, leave.onDateMillis);
  }

  @Override
  public int hashCode() {

    return Objects.hash(onDateMillis);
  }
}
