package com.edunexus.backend.lostfound;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "lost_Found")
public class LostFound {
	@Id
	@Column(name = "item_id")
	private String itemId;
	
	@Column(name = "item_name")
	private String itemName;

	@Column(name = "item_comments")
	private String itemDescription;

	@Column(name = "delivered")
	private boolean delivered;
	
	@Column(name = "date")
	private String date;
	

	@Column(name = "assignedTo")
	private String assignedTo;

	@Column(name = "image_url", length = 300)
	private String imageUrl;
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	
	public String getImageUrl() {
		return this.imageUrl;
	}
	
	public void setImageUrl(String img) {
		this.imageUrl = img;
	}
}
