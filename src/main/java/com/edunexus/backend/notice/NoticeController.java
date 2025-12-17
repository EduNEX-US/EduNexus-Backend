package com.edunexus.backend.notice;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@CrossOrigin(origins = "*")
public class NoticeController {
	
	@Autowired
    private NoticeService service;

    @GetMapping
    public List<Notice> getNotices() {
        return service.getAllNotice();
    }
    
    @PostMapping
    public ResponseEntity<?> postNotice(@RequestBody NoticeRequest req) {
        try {
            Notice saved = service.addNotice(req);
            return ResponseEntity.ok(Map.of("id", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving notice");
        }
    }
}
