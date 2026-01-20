package com.edunexus.backend.student;

public record StudentMeResponse(
        String id,
        String name,
        String email,
        long mobile,
        int studClass,
        String address,
        long altMobile,
        String guardian,
        String imageUrl
) {}
