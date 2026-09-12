package com.awol.employeetracker.model;

import java.io.Serializable;

public class Employee implements Serializable {
    private String id;
    private String name;
    private String email;
    private String department;
    private String role; // "EMPLOYEE" or "ADMIN"
    private String phone;
    private boolean isPresent;

    public Employee() {
    }

    public Employee(String id, String name, String email, String department, String role, String phone, boolean isPresent) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.role = role;
        this.phone = phone;
        this.isPresent = isPresent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
