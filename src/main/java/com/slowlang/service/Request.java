package com.slowlang.service;

import java.util.Set;

public class Request {
	
	private String body;
	private String type;
	
	public Request(String body, String type, Set<String> allowedTypes) {
		
		if(!allowedTypes.contains(type)) {
			
			throw new IllegalArgumentException("Invalid request type.");
		} 
		
		this.body = body;
		this.type = type;
	}
	
	public String getBody() {
		
		return body;
	}
	
	public String getType() {
		
		return type;
	}
}
