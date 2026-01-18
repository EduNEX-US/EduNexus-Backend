package com.edunexus.backend.teacher;

public class TeacherUpdateRequest {
    private String name;
    private String email;
    private Long mobile;
    private Integer exp;
    private String address;
    private String qualification;
    private Integer tClass;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getMobile() { return mobile; }
    public void setMobile(Long mobile) { this.mobile = mobile; }

    public Integer getExp() { return exp; }
    public void setExp(Integer exp) { this.exp = exp; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public Integer getTClass() { return tClass; }
    public void setTClass(Integer tClass) { this.tClass = tClass; }
}
