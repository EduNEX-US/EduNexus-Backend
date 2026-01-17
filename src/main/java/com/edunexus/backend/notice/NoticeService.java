package com.edunexus.backend.notice;

import java.util.List;
import java.util.Optional;
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
	
	public Notice getNoticeById(String id) {
		Optional<Notice> optionalNotice = repo.findById(id);
		
		if(!optionalNotice.isPresent()) {
			throw new RuntimeException("Notice not found");
		}
		Notice notice = optionalNotice.get();
		return notice;
	}
	
	public Notice updateNotice(String id, NoticeRequest req) {
	    Notice existing = repo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Notice not found"));

	    existing.setTitle(req.getTitle());
	    existing.setBody(req.getBody());
	    existing.setDate(req.getDate()); // or keep old date if you want

	    return repo.save(existing);
	}

	public void deleteNotice(String id) {
	    if (!repo.existsById(id)) {
	        throw new RuntimeException("Notice not found");
	    }
	    repo.deleteById(id);
	}

}
