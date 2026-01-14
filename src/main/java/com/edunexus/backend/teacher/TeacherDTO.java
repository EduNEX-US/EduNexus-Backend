package com.edunexus.backend.teacher;

public class TeacherDTO {

    private String id;
    private String name;
    private int role;
    // ✅ REQUIRED constructor
    public TeacherDTO(String id, String name, int role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public int getRole() {
    	return role;
    }
}
