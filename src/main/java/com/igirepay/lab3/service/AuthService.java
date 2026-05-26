package com.igirepay.lab3.service;

import com.igirepay.Lab2.dao.CustomerDAO;
import com.igirepay.Lab1.model.Customer;

public class AuthService {

    private CustomerDAO customerDAO = new CustomerDAO();

    public Customer login(String phone, String pin) {
        Customer customer = customerDAO.getCustomerByPhone(phone);

        if (customer == null) {
            System.out.println("Customer not found!");
            return null;
        }
        if (customer.validatePin(pin)) {
            System.out.println("Login successful! Welcome, " + customer.getFullName());
            return customer;
        } else {
            System.out.println("Wrong PIN!");
            return null;
        }
    }

    public boolean changePin(String phone, String oldPin, String newPin) {
        Customer customer = customerDAO.getCustomerByPhone(phone);

        if (customer == null) return false;

        if (!customer.validatePin(oldPin)) {
            System.out.println("Old PIN is incorrect!");
            return false;
        }
        customer.setPin(newPin);
        customerDAO.updateCustomer(customer);
        System.out.println("PIN changed successfully!");
        return true;
    }
}
