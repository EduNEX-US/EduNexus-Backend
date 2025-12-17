package com.edunexus.backend.teacher;

public class TeacherDTO {

    private String id;
    private String name;

    // ✅ REQUIRED constructor
    public TeacherDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
