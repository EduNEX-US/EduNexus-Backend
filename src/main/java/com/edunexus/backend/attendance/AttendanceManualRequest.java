package com.edunexus.backend.attendance;

import java.util.List;

public class AttendanceManualRequest {
    private int classId;          // teacher class
    private String date;          // "2026-01-18"
    private List<Entry> entries;  // absent/late list

    public static class Entry {
        private String studentId;
        private String status; // "ABSENT" or "LATE" (optional -> ABSENT)

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<Entry> getEntries() { return entries; }
    public void setEntries(List<Entry> entries) { this.entries = entries; }
}
