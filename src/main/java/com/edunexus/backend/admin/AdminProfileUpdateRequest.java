package com.edunexus.backend.admin;

public class AdminProfileUpdateRequest {
    private String name;
    private String email;
    private long mobile;
    private int exp;
    private String qualification;
    private String address;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public long getMobile() { return mobile; }
    public void setMobile(long mobile) { this.mobile = mobile; }

    public int getExp() { return exp; }
    public void setExp(int exp) { this.exp = exp; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
