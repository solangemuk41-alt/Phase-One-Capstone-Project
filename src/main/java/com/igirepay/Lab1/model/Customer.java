package com.igirepay.Lab1.model;

public class Customer {

    private int id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String pin;

    public Customer(int id, String fullName, String email, String phoneNumber, String pin) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.pin = pin;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPin() { return pin; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setPin(String pin) { this.pin = pin; }

    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", name=" + fullName + ", phone=" + phoneNumber + "}";
    }
}
