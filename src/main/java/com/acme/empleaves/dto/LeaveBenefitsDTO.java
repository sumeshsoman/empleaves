package com.acme.empleaves.dto;

import java.io.Serializable;

public class LeaveBenefitsDTO  implements Serializable{

    private String leaveType;
    private Integer earned;
    private String designation;

    public LeaveBenefitsDTO(String leaveType, Integer earned, String designation) {
        this.leaveType = leaveType;
        this.earned = earned;
        this.designation = designation;
    }

    public LeaveBenefitsDTO() {
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getEarned() {
        return earned;
    }

    public void setEarned(Integer earned) {
        this.earned = earned;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
