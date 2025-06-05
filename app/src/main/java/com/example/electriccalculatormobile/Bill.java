package com.example.electriccalculatormobile;
public class Bill {
    private int id;
    private String month;
    private double units;
    private double totalCharges;
    private double rebate;
    private double finalCost;

    // Default constructor
    public Bill() {
    }

    // Parameterized constructor
    public Bill(String month, double units, double totalCharges, double rebate, double finalCost) {
        this.month = month;
        this.units = units;
        this.totalCharges = totalCharges;
        this.rebate = rebate;
        this.finalCost = finalCost;
    }

    // Getter for ID
    public int getId() {
        return id;
    }

    // Setter for ID
    public void setId(int id) {
        this.id = id;
    }

    // Getter for month
    public String getMonth() {
        return month;
    }

    // Setter for month
    public void setMonth(String month) {
        this.month = month;
    }

    // Getter for units
    public double getUnits() {
        return units;
    }

    // Setter for units
    public void setUnits(double units) {
        this.units = units;
    }

    // Getter for totalCharges
    public double getTotalCharges() {
        return totalCharges;
    }

    // Setter for totalCharges
    public void setTotalCharges(double totalCharges) {
        this.totalCharges = totalCharges;
    }

    // Getter for rebate
    public double getRebate() {
        return rebate;
    }

    // Setter for rebate
    public void setRebate(double rebate) {
        this.rebate = rebate;
    }

    // Getter for finalCost
    public double getFinalCost() {
        return finalCost;
    }

    // Setter for finalCost
    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }
}