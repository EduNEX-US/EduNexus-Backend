package com.edunexus.backend.lostfound;

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
@RequestMapping("/lostfound")
@CrossOrigin("*")
public class LostfoundController {
	
	@Autowired
	private LostFoundService lostFoundService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addLostItem(@RequestBody LostFoundRequest req) {
		try {
			LostFound saved = lostFoundService.addLostItem(req);
			return ResponseEntity.ok(Map.of("message", "Item added successfully",  "itemId", saved.getItemId()));
		}
		catch(Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Error adding the item!"));
		}
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getAllItem() { // this will bring in the item Id from front end once the item is set
		try {
			List<LostFound> allitems = lostFoundService.getAllItems();
			return ResponseEntity.ok(Map.of("message", "Items fetched successfully", "items",allitems));
		}
		catch(Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", "Error adding the item!"));
		}
		
	}
	
}
