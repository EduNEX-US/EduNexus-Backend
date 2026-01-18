package com.edunexus.backend.attendance;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(
    name = "attendance_session",
    uniqueConstraints = @UniqueConstraint(columnNames = {"class_id", "date"})
)
public class AttendanceSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="class_id", nullable=false)
    private int classId;

    @Column(name="date", nullable=false)
    private LocalDate date;

    @Column(name="teacher_id", nullable=false, length=50)
    private String teacherId;

    public Long getId() { return id; }

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
}
