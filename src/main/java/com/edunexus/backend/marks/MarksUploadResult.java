package com.edunexus.backend.marks;

import java.util.ArrayList;
import java.util.List;

public class MarksUploadResult {
    private int total;
    private int success;
    private int failed;
    private List<MarksUploadRowResult> rows = new ArrayList<>();

    public MarksUploadResult(int total) { this.total = total; }

    public int getTotal() { return total; }
    public int getSuccess() { return success; }
    public int getFailed() { return failed; }
    public List<MarksUploadRowResult> getRows() { return rows; }

    public void incSuccess() { success++; }
    public void incFailed() { failed++; }
    public void addRow(MarksUploadRowResult row) { rows.add(row); }
}
