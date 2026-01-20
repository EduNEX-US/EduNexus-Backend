package com.edunexus.backend.student;

public class StudentUpdateRequest {
    private String name;
    private String email;
    private Long mobile;
    private String address;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getMobile() { return mobile; }
    public void setMobile(Long mobile) { this.mobile = mobile; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
