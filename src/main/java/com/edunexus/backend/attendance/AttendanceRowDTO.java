package com.edunexus.backend.attendance;

public class AttendanceRowDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String guardian;
    private String status; // PRESENT/ABSENT/LATE

    public AttendanceRowDTO(String id, String name, String email, String address, String guardian, String status) {
        this.id = id; this.name = name; this.email = email; this.address = address; this.guardian = guardian; this.status = status;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getGuardian() { return guardian; }
    public String getStatus() { return status; }
}
