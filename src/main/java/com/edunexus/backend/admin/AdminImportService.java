package com.edunexus.backend.admin;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.edunexus.backend.teacher.CreateTeacherRequest;
import com.edunexus.backend.teacher.TeacherService;

@Service
public class AdminImportService {

    @Autowired
    private TeacherService teacherService;

    public ImportResult importTeachersCsv(MultipartFile file, String defaultPassword) {
        try (var reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            // ✅ read once
            List<CSVRecord> records = parser.getRecords();
            ImportResult result = new ImportResult(records.size());

            for (CSVRecord record : records) {
                String name = safeGet(record, "tName");

                try {
                    CreateTeacherRequest req = new CreateTeacherRequest();
                    req.setTeacherName(name);
                    req.setTeacherEmail(safeGet(record, "email"));
                    req.setTeacherMob(parseLong(safeGet(record, "tMob")));
                    req.setTeacherExp(parseInt(safeGet(record, "exp")));
                    req.setTeacherAdd(safeGet(record, "tAddress"));

                    // keep as string like "7,8,10"
                    req.setTeacherClass(parseInt(safeGet(record, "tClass")));

                    req.setTeacherQualification(safeGet(record, "qualification"));

                    String role = "teacher";
                    if (record.isMapped("role")) {
                        String r = safeGet(record, "role");
                        if (r != null && !r.isBlank()) role = r.trim().toLowerCase();
                    }

                    req.setRole(role);
                    req.setPassword(defaultPassword);

                    String id = teacherService.createTeacherAndReturnId(req);

                    result.incSuccess();
                    result.addRow(new ImportRowResult(name, id, role, "OK", null));
                } catch (Exception ex) {
                    result.incFailed();
                    result.addRow(new ImportRowResult(name, null, null, "FAILED", ex.getMessage()));
                }
            }

            return result;

        } catch (Exception e) {
            ImportResult r = new ImportResult(0);
            r.incFailed();
            r.addRow(new ImportRowResult(null, null, null, "FAILED", "CSV_READ_FAILED: " + e.getMessage()));
            return r;
        }
    }

    private static String safeGet(CSVRecord record, String key) {
        if (!record.isMapped(key)) return "";
        String val = record.get(key);
        if (val == null) return "";
        // ✅ handle BOM in first header value (common issue)
        return val.replace("\uFEFF", "").trim();
    }

    private static long parseLong(String s) {
        if (s == null || s.isBlank()) return 0L;
        return Long.parseLong(s.trim());
    }

    private static int parseInt(String s) {
        if (s == null || s.isBlank()) return 0;
        return Integer.parseInt(s.trim());
    }

    private static String normalizeClasses(String s) {
        // "7, 8,10" -> "7,8,10"
        return s == null ? "" : s.trim().replaceAll("\\s+", "");
    }
}
