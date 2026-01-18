package com.edunexus.backend.attendance;

public class AttendanceUploadRowResult {
    private String studentId;
    private String status; // ABSENT/LATE
    private String result; // OK/FAILED
    private String error;

    public AttendanceUploadRowResult(String studentId, String status, String result, String error) {
        this.studentId = studentId;
        this.status = status;
        this.result = result;
        this.error = error;
    }

    public String getStudentId() { return studentId; }
    public String getStatus() { return status; }
    public String getResult() { return result; }
    public String getError() { return error; }
}
