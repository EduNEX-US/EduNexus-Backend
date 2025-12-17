package com.edunexus.backend.student;

public class StudentDTO {

    private String id;
    private String name;

    // ✅ REQUIRED constructor
    public StudentDTO(String id, String name) {
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
