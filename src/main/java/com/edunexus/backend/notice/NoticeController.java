package com.edunexus.backend.notice;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getNoticeById(@PathVariable String id) {
    	try {
    		Notice saved = service.getNoticeById(id);
    		return ResponseEntity.ok(Map.of("notice", saved));
    	} catch(Exception e) {
    		return ResponseEntity.status(500).body("Error fetching the notice");
    	}
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotice(@PathVariable String id, @RequestBody NoticeRequest req) {
        try {
            Notice updated = service.updateNotice(id, req);
            return ResponseEntity.ok(Map.of("message", "Notice updated", "notice", updated));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating notice");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotice(@PathVariable String id) {
        try {
            service.deleteNotice(id);
            return ResponseEntity.ok(Map.of("message", "Notice deleted", "id", id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error deleting notice");
        }
    }

    
}
