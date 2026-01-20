package com.edunexus.backend.attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceUploadResult {
    private int total;
    private int success;
    private int failed;
    private List<AttendanceUploadRowResult> rows = new ArrayList<>();

    public AttendanceUploadResult() {}
    public AttendanceUploadResult(int total) { this.total = total; }

    public void incSuccess() { success++; }
    public void incFailed() { failed++; }

    public void addRow(AttendanceUploadRowResult r) { rows.add(r); }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public int getSuccess() { return success; }
    public void setSuccess(int success) { this.success = success; }

    public int getFailed() { return failed; }
    public void setFailed(int failed) { this.failed = failed; }

    public List<AttendanceUploadRowResult> getRows() { return rows; }
    public void setRows(List<AttendanceUploadRowResult> rows) { this.rows = rows; }
}
