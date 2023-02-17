package com.slowlang.service;

import org.springframework.stereotype.Service;

@Service
public class ExecutionEngineService {
	
	public String execute(byte[] bytecode) {
		
		try {
			
			Thread.sleep(2000);
		
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		return "Mockup response";
	}
}
