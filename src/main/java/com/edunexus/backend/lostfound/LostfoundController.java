package com.edunexus.backend.lostfound;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/lostfound")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LostfoundController {
	
	@Autowired
	private LostFoundService lostFoundService;
	
	@PostMapping(value = "/add", consumes = "multipart/form-data")
	public ResponseEntity<?> addLostItem(
	        @RequestParam("name") String name,
	        @RequestParam(value = "description", required = false) String description,
	        @RequestParam("assignedTo") String assignedTo,
	        @RequestParam("date") String date,
	        @RequestParam(value = "image", required = false) MultipartFile image
	) {
	    try {
	        String imageUrl = null;
	        if (image != null && !image.isEmpty()) {
	            imageUrl = lostFoundService.saveImage(image); // ✅ store file + return "/uploads/.."
	        }

	        LostFound saved = lostFoundService.addLostItemMultipart(name, description, assignedTo, date, imageUrl);

	        return ResponseEntity.ok(Map.of(
	                "message", "Item added successfully",
	                "itemId", saved.getItemId(),
	                "imageUrl", imageUrl == null ? "" : imageUrl
	        ));
	    } catch (Exception e) {
	        e.printStackTrace(); // ✅ add this
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
	
	@DeleteMapping("/delete/{itemId}")
	public ResponseEntity<?> deleteItem(@PathVariable String itemId) {
	    try {
	        lostFoundService.deleteItemById(itemId);
	        return ResponseEntity.ok(Map.of("message", "Item deleted successfully", "itemId", itemId));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(Map.of("error", "Error deleting the item!"));
	    }
	}

	@PutMapping(value = "/update/{itemId}", consumes = "multipart/form-data")
	public ResponseEntity<?> updateLostItem(
	        @PathVariable String itemId,
	        @RequestParam("name") String name,
	        @RequestParam(value = "description", required = false) String description,
	        @RequestParam("assignedTo") String assignedTo,
	        @RequestParam("date") String date,
	        @RequestParam(value = "image", required = false) MultipartFile image
	) {
	    try {
	        LostFound updated = lostFoundService.updateItemMultipart(itemId, name, description, assignedTo, date, image);

	        return ResponseEntity.ok(Map.of(
	                "message", "Item updated successfully",
	                "itemId", updated.getItemId(),
	                "imageUrl", updated.getImageUrl() == null ? "" : updated.getImageUrl()
	        ));
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(Map.of("error", "Error updating the item!"));
	    }
	}

	
}
