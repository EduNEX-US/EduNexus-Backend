package com.edunexus.backend.marks;

public class MarksUploadRowResult {
    private String studentId;
    private String status; // OK/FAILED
    private String action; // INSERTED/UPDATED/-
    private String error;

    public MarksUploadRowResult(String studentId, String status, String action, String error) {
        this.studentId = studentId;
        this.status = status;
        this.action = action;
        this.error = error;
    }

    public String getStudentId() { return studentId; }
    public String getStatus() { return status; }
    public String getAction() { return action; }
    public String getError() { return error; }
}
