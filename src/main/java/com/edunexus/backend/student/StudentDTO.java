package com.edunexus.backend.student;

public class StudentDTO {

    private String id;
    private String name;
    private String email;
    private String address;
    private String guardian;
    public long mobile;
    // ✅ Constructor
    public StudentDTO(String id, String name, String email, String address, String guardian, long mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.guardian = guardian;
        this.mobile = mobile;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getGuardian() { return guardian; }
    public long getMobile() { return mobile;}
}
