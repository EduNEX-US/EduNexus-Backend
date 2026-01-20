package com.edunexus.backend.attendance;

public class AttendanceUploadRowResult {
    private String studentId;
    private String status; // OK / FAILED
    private String action; // INSERTED / UPDATED / -
    private String error;

    public AttendanceUploadRowResult() {}

    public AttendanceUploadRowResult(String studentId, String status, String action, String error) {
        this.studentId = studentId;
        this.status = status;
        this.action = action;
        this.error = error;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
