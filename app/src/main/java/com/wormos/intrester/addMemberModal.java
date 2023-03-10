package com.wormos.intrester;

public class addMemberModal {
        String premiumPlan , memberName, amountRemaining, duration;

    public addMemberModal() {
    }

    public addMemberModal(String premiumPlan, String memberName, String amountRemaining, String duration) {
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
}
