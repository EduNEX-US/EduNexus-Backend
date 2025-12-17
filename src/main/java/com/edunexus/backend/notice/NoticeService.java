package com.edunexus.backend.notice;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {
	
	@Autowired
	private NoticeRepository repo;
	
	public List<Notice> getAllNotice() {
		return repo.findAll();
	}
	
	public Notice addNotice(NoticeRequest req) {
		Notice n = new Notice();
		
		 n.setId(UUID.randomUUID().toString());
	     n.setTitle(req.getTitle());
	     n.setBody(req.getBody());
	     n.setDate(req.getDate());
	        
	    return repo.save(n);   
	}
}
