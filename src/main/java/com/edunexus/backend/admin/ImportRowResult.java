package com.edunexus.backend.admin;

public class ImportRowResult {
    private String name;
    private String id;
    private String role;   // teacher/admin
    private String status; // OK/FAILED
    private String error;

    public ImportRowResult(String name, String id, String role, String status, String error) {
        this.name = name;
        this.id = id;
        this.role = role;
        this.status = status;
        this.error = error;
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public String getError() { return error; }
}
