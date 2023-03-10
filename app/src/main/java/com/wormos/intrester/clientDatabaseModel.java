package com.wormos.intrester;

public class clientDatabaseModel {
        String premiumPlan , memberName, amountRemaining, duration, accountNo, amountCollected, address,
                phone, dateOfOpen, dateOfMaturity, plan, placeId;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(String amountCollected) {
        this.amountCollected = amountCollected;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfOpen() {
        return dateOfOpen;
    }

    public void setDateOfOpen(String dateOfOpen) {
        this.dateOfOpen = dateOfOpen;
    }

    public String getDateOfMaturity() {
        return dateOfMaturity;
    }

    public void setDateOfMaturity(String dateOfMaturity) {
        this.dateOfMaturity = dateOfMaturity;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public clientDatabaseModel() {
    }

    public clientDatabaseModel(String premiumPlan, String memberName, String amountRemaining, String duration) {
        this.premiumPlan = premiumPlan;
        this.memberName = memberName;
        this.amountRemaining = amountRemaining;
        this.duration = duration;
    }

    public String getPremiumPlan() {
        return premiumPlan;
    }

    public void setPremiumPlan(String premiumPlan) {
        this.premiumPlan = premiumPlan;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(String amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public clientDatabaseModel(String premiumPlan, String memberName, String amountRemaining,
                               String duration, String accountNo, String amountCollected, String address,
                               String phone, String dateOfOpen, String dateOfMaturity, String plan, String placeId) {
        this.premiumPlan = premiumPlan;
        this.memberName = memberName;
        this.amountRemaining = amountRemaining;
        this.duration = duration;
        this.accountNo = accountNo;
        this.amountCollected = amountCollected;
        this.address = address;
        this.phone = phone;
        this.dateOfOpen = dateOfOpen;
        this.dateOfMaturity = dateOfMaturity;
        this.plan = plan;
        this.placeId = placeId;
    }
}
