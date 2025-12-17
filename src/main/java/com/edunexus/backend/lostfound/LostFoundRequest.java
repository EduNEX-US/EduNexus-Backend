package com.edunexus.backend.lostfound;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LostFoundRequest {

	@JsonProperty("name")
	private String itemName;
	
	@JsonProperty("description")
	private String itemDescription;
	
	@JsonProperty("assignedTo")
	private String assignedTo;
	
	@JsonProperty("date")
	private String itemDate;

	
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

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getItemDate() {
		return itemDate;
	}

	public void setItemDate(String itemDate) {
		this.itemDate = itemDate;
	}
	
	
}
