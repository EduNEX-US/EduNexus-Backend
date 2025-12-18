package com.edunexus.backend.marks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.edunexus.backend.student.Student;
import com.edunexus.backend.student.StudentRepository;

@Service
public class AutomationService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    public String processMarksCSV(MultipartFile file) {
        if (file.isEmpty()) {
            return "Please upload a valid CSV file.";
        }

        List<Marks> marksList = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            // Optional: Skip header if exists. Assuming NO header for now based on simple parsing, 
            // or we can add a simple check. Let's try to parse every line. 
            // If the first line fails (e.g. contains "english"), we just catch exception and continue.
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                // Expecting 8 columns: student_id + 7 subjects
                if (data.length < 8) {
                    failureCount++;
                    continue; // Skip malformed rows
                }

                try {
                    String studentId = data[0].trim();
                    Optional<Student> studentOpt = studentRepository.findById(studentId);

                    if (studentOpt.isPresent()) {
                        Student student = studentOpt.get();
                        Marks marks = new Marks();
                        marks.setStudent(student);
                        
                        marks.setEnglish(Double.parseDouble(data[1].trim()));
                        marks.setHindi(Double.parseDouble(data[2].trim()));
                        marks.setMath(Double.parseDouble(data[3].trim()));
                        marks.setScience(Double.parseDouble(data[4].trim()));
                        marks.setSocialScience(Double.parseDouble(data[5].trim()));
                        marks.setGeneralKnowledge(Double.parseDouble(data[6].trim()));
                        marks.setComputer(Double.parseDouble(data[7].trim()));
                        
                        // Default to today
                        marks.setResultDate(LocalDate.now());

                        marksList.add(marks);
                        successCount++;
                    } else {
                        // Student ID not found in DB
                        failureCount++;
                    }
                } catch (NumberFormatException e) {
                    // Header row or invalid number
                    failureCount++;
                } catch (Exception e) {
                    failureCount++;
                }
            }
            
            marksRepository.saveAll(marksList);
            return "Upload Complete. Success: " + successCount + ", Failed: " + failureCount;

        } catch (IOException e) {
            return "Error processing file: " + e.getMessage();
        }
    }
    
    public List<Marks> getAllMarks() {
    	List<Marks> marks = marksRepository.findAll();
    	if(marks.isEmpty()) {
    		throw new RuntimeException("No marks found");
    	}
    	return marks;
    }
}
