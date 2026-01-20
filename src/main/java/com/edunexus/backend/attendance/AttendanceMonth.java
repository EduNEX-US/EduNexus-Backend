package com.edunexus.backend.attendance;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "attendance_monthly",
    uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "year_month"})
)
public class AttendanceMonth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // store plain studentId (no FK needed, easier)
    @Column(name = "student_id", nullable = false, length = 50)
    private String studentId;

    @Column(name = "class_id", nullable = false)
    private int classId;

    // format: "2026-01"
    @Column(name = "`year_month`", nullable = false, length = 7)
    private String yearMonth;

    @Column(name = "total_days", nullable = false)
    private int totalDays;

    @Column(name = "present", nullable = false)
    private int present;

    @Column(name = "absent", nullable = false)
    private int absent;

    @Column(name = "late", nullable = false)
    private int late;

    @Column(name = "updated_by", nullable = false, length = 50)
    private String updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ----- getters/setters -----
    public Long getId() { return id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public String getYearMonth() { return yearMonth; }
    public void setYearMonth(String yearMonth) { this.yearMonth = yearMonth; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public int getPresent() { return present; }
    public void setPresent(int present) { this.present = present; }

    public int getAbsent() { return absent; }
    public void setAbsent(int absent) { this.absent = absent; }

    public int getLate() { return late; }
    public void setLate(int late) { this.late = late; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
