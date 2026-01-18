package com.edunexus.backend.attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceUploadResult {
    private int total;
    private int success;
    private int failed;
    private List<AttendanceUploadRowResult> rows = new ArrayList<>();

    public AttendanceUploadResult(int total) { this.total = total; }

    public int getTotal() { return total; }
    public int getSuccess() { return success; }
    public int getFailed() { return failed; }
    public List<AttendanceUploadRowResult> getRows() { return rows; }

    public void incSuccess() { success++; }
    public void incFailed() { failed++; }
    public void addRow(AttendanceUploadRowResult row) { rows.add(row); }
}
