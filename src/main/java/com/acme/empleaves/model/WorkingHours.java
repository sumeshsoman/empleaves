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
public class WorkingHours implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long logOnTime;
  private Long logOffTime;
  private Long workTime;

  @OneToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;

  public WorkingHours() {}

  public WorkingHours(Long logOnTime, Long logOffTime, Long workTime) {
    this.logOnTime = logOnTime;
    this.logOffTime = logOffTime;
    this.workTime = workTime;
  }

  public Long getId() {
    return id;
  }

  public Long getLogOnTime() {
    return logOnTime;
  }

  public void setLogOnTime(Long logOnTime) {
    this.logOnTime = logOnTime;
  }

  public Long getLogOffTime() {
    return logOffTime;
  }

  public void setLogOffTime(Long logOffTime) {
    this.logOffTime = logOffTime;
  }

  public Long getWorkTime() {
    return workTime;
  }

  public void setWorkTime(Long workTime) {
    this.workTime = workTime;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  @Override
  public String toString() {
    return "WorkingHours{"
        + "logOnTime="
        + logOnTime
        + ", logOffTime="
        + logOffTime
        + ", workTime="
        + workTime
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    WorkingHours that = (WorkingHours) o;
    return Objects.equals(logOnTime, that.logOnTime) && Objects.equals(logOffTime, that.logOffTime);
  }

  @Override
  public int hashCode() {

    return Objects.hash(logOnTime, logOffTime);
  }
}
