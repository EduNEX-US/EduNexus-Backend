package com.edunexus.backend.marks;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/marks")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend access
public class MarksController {

    @Autowired
    private AutomationService automationService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMarks(@RequestParam("file") MultipartFile file) {
        String result = automationService.processMarksCSV(file);
        if (result.startsWith("Error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping
    public ResponseEntity<?> getMarks() {
    	try {
    		List<Marks> marks = automationService.getAllMarks();
    		return ResponseEntity.status(HttpStatus.OK).body(marks);
    	}
    	catch(Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
    	}
    }
}
