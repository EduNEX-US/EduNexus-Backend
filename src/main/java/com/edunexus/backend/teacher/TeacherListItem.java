package com.edunexus.backend.teacher;

public class TeacherListItem {
    private String id;
    private String name;
    private String email;
    private long mobile;
    private String address;
    private int experience;
    private String teacherClass;
    private String qualification;
    private int isAdmin; // 1 admin, 0 teacher

    public TeacherListItem() {}

    public TeacherListItem(
            String id,
            String name,
            String email,
            long mobile,
            String address,
            int experience,
            String teacherClass,
            String qualification,
            int isAdmin
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        this.experience = experience;
        this.teacherClass = teacherClass;
        this.qualification = qualification;
        this.isAdmin = isAdmin;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public long getMobile() { return mobile; }
    public String getAddress() { return address; }
    public int getExperience() { return experience; }
    public String getTeacherClass() { return teacherClass; }
    public String getQualification() { return qualification; }
    public int getIsAdmin() { return isAdmin; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setMobile(long mobile) { this.mobile = mobile; }
    public void setAddress(String address) { this.address = address; }
    public void setExperience(int experience) { this.experience = experience; }
    public void setTeacherClass(String teacherClass) { this.teacherClass = teacherClass; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setIsAdmin(int isAdmin) { this.isAdmin = isAdmin; }
}
