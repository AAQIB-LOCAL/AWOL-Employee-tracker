package com.awol.employeetracker.model;

import java.io.Serializable;

public class AttendanceRecord implements Serializable {
    private long id;
    private String employeeId;
    private String employeeName;
    private String date;
    private String checkInTime;
    private String checkOutTime;
    private String status; // "PRESENT", "ABSENT", "HALF_DAY"
    private String locationIn;
    private String locationOut;
    private boolean isSynced;

    public AttendanceRecord() {
    }

    public AttendanceRecord(long id, String employeeId, String employeeName, String date,
                            String checkInTime, String checkOutTime, String status,
                            String locationIn, String locationOut, boolean isSynced) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.date = date;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.locationIn = locationIn;
        this.locationOut = locationOut;
        this.isSynced = isSynced;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocationIn() {
        return locationIn;
    }

    public void setLocationIn(String locationIn) {
        this.locationIn = locationIn;
    }

    public String getLocationOut() {
        return locationOut;
    }

    public void setLocationOut(String locationOut) {
        this.locationOut = locationOut;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
