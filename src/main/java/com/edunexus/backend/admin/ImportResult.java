package com.edunexus.backend.admin;

import java.util.ArrayList;
import java.util.List;

public class ImportResult {
    private int total;
    private int success;
    private int failed;
    private List<ImportRowResult> rows = new ArrayList<>();

    public ImportResult(int total) { this.total = total; }

    public int getTotal() { return total; }
    public int getSuccess() { return success; }
    public int getFailed() { return failed; }
    public List<ImportRowResult> getRows() { return rows; }

    public void incSuccess() { success++; }
    public void incFailed() { failed++; }

    public void addRow(ImportRowResult row) { rows.add(row); }
}
