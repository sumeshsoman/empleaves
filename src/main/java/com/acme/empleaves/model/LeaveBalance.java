package com.acme.empleaves.model;

import java.io.Serializable;
import java.util.Objects;

public class LeaveBalance implements Serializable {

    private LeaveType leaveType;
    private Double earned;
    private Double taken;
    private Double balance;

    public LeaveBalance(LeaveType leaveType, Double earned, Double taken, Double balance) {
        this.leaveType = leaveType;
        this.earned = earned;
        this.taken = taken;
        this.balance = balance;
    }

    public LeaveBalance(LeaveType leaveType) {
        this.leaveType = leaveType;
        this.balance = 0.0;
        this.earned = 0.0;
        this.taken = 0.0;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Double getEarned() {
        return earned;
    }

    public void setEarned(Double earned) {
        this.earned = earned;
    }

    public Double getTaken() {
        return taken;
    }

    public void setTaken(Double taken) {
        this.taken = taken;
    }

    public void addToTaken(int number){
        this.taken += number;
    }

    public Double getBalance() {
        this.balance =  this.earned - this.taken;
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeaveBalance that = (LeaveBalance) o;
        return leaveType == that.leaveType;
    }

    @Override
    public int hashCode() {

        return Objects.hash(leaveType);
    }
}
