package com.edunexus.backend.admin;

public class TeacherCsvRow {
    private String role;          // "teacher" or "admin"
    private String tName;
    private String email;
    private String tMob;
    private String exp;
    private String tAddress;
    private String tClass;
    private String qualification;

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getTName() { return tName; }
    public void setTName(String tName) { this.tName = tName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTMob() { return tMob; }
    public void setTMob(String tMob) { this.tMob = tMob; }

    public String getExp() { return exp; }
    public void setExp(String exp) { this.exp = exp; }

    public String getTAddress() { return tAddress; }
    public void setTAddress(String tAddress) { this.tAddress = tAddress; }

    public String getTClass() { return tClass; }
    public void setTClass(String tClass) { this.tClass = tClass; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
}
