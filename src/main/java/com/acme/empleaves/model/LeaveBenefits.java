package com.acme.empleaves.model;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import java.util.HashMap;
import java.util.Map;

@Entity
public class LeaveBenefits {

  @ElementCollection
  @MapKeyColumn(name = "leavetype")
  @Column(name = "maxavailability")
  @CollectionTable(
    name = "leavetype_max_eligibility",
    joinColumns = @JoinColumn(name = "benefits_id")
  )
  Map<String, Integer> maxLeaveEligibilityMap = new HashMap<String, Integer>();
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String designation;

  public LeaveBenefits() {}

  public Long getId() {
    return id;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public Map<String, Integer> getMaxLeaveEligibilityMap() {
    return maxLeaveEligibilityMap;
  }

  public void setMaxLeaveEligibilityMap(Map<String, Integer> maxLeaveEligibilityMap) {
    this.maxLeaveEligibilityMap = maxLeaveEligibilityMap;
  }
}

