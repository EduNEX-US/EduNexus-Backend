package com.edunexus.backend.attendance;

public record AttendanceStudentMonthDTO(
        String yearMonth,
        int totalDays,
        int present,
        int absent,
        int late,
        double percentage
) {}
