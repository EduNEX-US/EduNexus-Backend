package com.edunexus.backend.teacher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.edunexus.backend.admin.ImportResult;
import com.edunexus.backend.admin.ImportRowResult;
import com.edunexus.backend.student.CreateStudentRequest;
import com.edunexus.backend.student.StudentService;

@Service
public class TeacherStudentImportService {

    private static final String DEFAULT_PASSWORD = "Welcom@123";

    @Autowired private TeacherRepository teacherRepo;
    @Autowired private StudentService studentService;

    public ImportResult importMyStudentsCsv(String teacherId, MultipartFile file) {

        // 1) get teacher class
        Teacher teacher = teacherRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found: " + teacherId));

        int teacherClass = teacher.getTeacher_class();
        if (teacherClass == 0 ) {
            throw new RuntimeException("Teacher has no class assigned");
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            // read all lines into memory? no. just count by streaming:
            // We'll compute total as we go (or do a first pass). Easiest: read into list -> but can be big.
            // Practical approach: start with total=0 and adjust your ImportResult to accept mutable total,
            // BUT your ImportResult constructor needs total.
            //
            // So we’ll do a quick first pass counting lines (excluding header) safely.
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV: " + e.getMessage());
        }

        // ✅ two-pass approach to get total
        int total = countDataRows(file);
        ImportResult result = new ImportResult(total);

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String header = br.readLine(); // skip header
            if (header == null) return result;

            String line;
            int rowNum = 1; // header is row 1

            while ((line = br.readLine()) != null) {
                rowNum++;
                if (line.trim().isEmpty()) continue;

                // name,email,mobile,altMob,guardian,address
                String[] cols = line.split(",", -1);
                if (cols.length < 6) {
                    result.incFailed();
                    result.addRow(new ImportRowResult(
                            "ROW_" + rowNum,
                            "",
                            "student",
                            "FAILED",
                            "Row " + rowNum + ": Expected 6 columns, got " + cols.length
                    ));
                    continue;
                }

                String name = cols[0].trim();
                String email = cols[1].trim();
                String mobileStr = cols[2].trim();
                String altMobStr = cols[3].trim();
                String guardian = cols[4].trim();
                String address = cols[5].trim();

                try {
                    // basic validation
                    if (name.isBlank() || email.isBlank() || mobileStr.isBlank() || guardian.isBlank() || address.isBlank()) {
                        throw new RuntimeException("Missing required fields");
                    }

                    long mobile = Long.parseLong(mobileStr);
                    long altMob = altMobStr.isBlank() ? 0L : Long.parseLong(altMobStr);

                    CreateStudentRequest req = new CreateStudentRequest();
                    req.setStudentName(name);
                    req.setStudentEmail(email);
                    req.setStudentMobile(mobile);
                    req.setStudentAltMobile(altMob);
                    req.setStudentGuardian(guardian);
                    req.setStudentAddress(address);

                    // ✅ force class from teacher
                    req.setStudentClass(teacherClass);

                    // ✅ default password
                    req.setStudentPassword(DEFAULT_PASSWORD);
                    req.setRole("student");

                    String newId = studentService.createStudentAndReturnId(req);

                    result.incSuccess();
                    result.addRow(new ImportRowResult(
                            name,
                            newId,
                            "student",
                            "OK",
                            ""
                    ));

                } catch (Exception ex) {
                    result.incFailed();
                    result.addRow(new ImportRowResult(
                            name.isBlank() ? ("ROW_" + rowNum) : name,
                            "",
                            "student",
                            "FAILED",
                            "Row " + rowNum + ": " + ex.getMessage()
                    ));
                }
            }

            return result;

        } catch (Exception e) {
            // if whole file breaks
            result.incFailed();
            result.addRow(new ImportRowResult(
                    "FILE",
                    "",
                    "student",
                    "FAILED",
                    "Import failed: " + e.getMessage()
            ));
            return result;
        }
    }

    private int countDataRows(MultipartFile file) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String header = br.readLine(); // header
            if (header == null) return 0;

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) count++;
            }
        } catch (Exception ignored) {}
        return count;
    }
}
