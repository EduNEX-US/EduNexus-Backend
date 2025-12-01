package com.edunexus.backend;

public class APIResponse {
	String message;
	
	public APIResponse(String msg) {
		this.message = msg;
	}
	public String getMessage() {
		return this.message;
	}
}
