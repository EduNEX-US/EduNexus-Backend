package com.edunexus.backend.attendance;

public class AttendanceSummaryDTO {
    private int totalDays;
    private int present;
    private int absent;
    private int late;
    private double percentage; // present/totalDays*100

    public AttendanceSummaryDTO(int totalDays, int present, int absent, int late) {
        this.totalDays = totalDays;
        this.present = present;
        this.absent = absent;
        this.late = late;
        this.percentage = totalDays <= 0 ? 0.0 : (present * 100.0 / totalDays);
    }

    public int getTotalDays() { return totalDays; }
    public int getPresent() { return present; }
    public int getAbsent() { return absent; }
    public int getLate() { return late; }
    public double getPercentage() { return percentage; }
}
