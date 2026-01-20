package com.edunexus.backend.attendance;

public record AttendanceTeacherRowDTO(
        String studentId,
        String name,
        String email,
        String address,
        String guardian,
        String mobile,

        int classId,
        String yearMonth,

        Integer totalDays,
        Integer present,
        Integer absent,
        Integer late,

        boolean uploaded
) {}
