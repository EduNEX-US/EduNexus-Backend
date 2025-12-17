package com.edunexus.backend.lostfound;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class LostFoundService {
	@Autowired
	private LostFoundRepository lostFoundRepo; 
	
	
	public LostFound addLostItem(@RequestBody LostFoundRequest req) {
		String uniqueItemId = "ITM"+UUID.randomUUID().toString().substring(0,5).toUpperCase();
		
		LostFound lostfound = new LostFound();
		lostfound.setItemId(uniqueItemId);
		lostfound.setItemName(req.getItemName());
		lostfound.setItemComments(req.getItemDescription());
		lostfound.setDate(req.getItemDate());
		lostfound.setAssignedTo(req.getAssignedTo());
		lostfound.setDelivered(false);
		return lostFoundRepo.save(lostfound);
	}
	
	public List<LostFound> getAllItems() {
		List<LostFound> lostitems = new ArrayList<>();
		lostitems = lostFoundRepo.findAll();
		
		return lostitems;
	}
	
}
