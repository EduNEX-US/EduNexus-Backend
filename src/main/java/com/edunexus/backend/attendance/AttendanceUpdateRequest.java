package com.edunexus.backend.attendance;

public class AttendanceUpdateRequest {
    private Integer totalDays;
    private Integer present;
    private Integer absent;
    private Integer late;

    public Integer getTotalDays() { return totalDays; }
    public void setTotalDays(Integer totalDays) { this.totalDays = totalDays; }

    public Integer getPresent() { return present; }
    public void setPresent(Integer present) { this.present = present; }

    public Integer getAbsent() { return absent; }
    public void setAbsent(Integer absent) { this.absent = absent; }

    public Integer getLate() { return late; }
    public void setLate(Integer late) { this.late = late; }
}
