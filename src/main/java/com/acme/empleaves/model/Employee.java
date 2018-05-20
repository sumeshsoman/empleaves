package com.acme.empleaves.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Employee implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String firstName;
  private String lastName;
  private String designation;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private Set<Leave> myAppliedLeaves = new HashSet<>();

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private Set<WorkingHours> myWorkingHours = new HashSet<>();

  public Employee() {}

  public Employee(String firstName, String lastName, String designation) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.designation = designation;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public Set<Leave> getMyAppliedLeaves() {
    return myAppliedLeaves;
  }

  public void setMyAppliedLeaves(Set<Leave> myAppliedLeaves) {
    this.myAppliedLeaves = myAppliedLeaves;
  }

  public Set<WorkingHours> getMyWorkingHours() {
    return myWorkingHours;
  }

  public void setMyWorkingHours(Set<WorkingHours> myWorkingHours) {
    this.myWorkingHours = myWorkingHours;
  }

  @Override
  public String toString() {
    return "Employee{"
        + "firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", designation='"
        + designation
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Employee employee = (Employee) o;
    return Objects.equals(firstName, employee.firstName)
        && Objects.equals(lastName, employee.lastName);
  }

  @Override
  public int hashCode() {

    return Objects.hash(firstName, lastName);
  }
}
