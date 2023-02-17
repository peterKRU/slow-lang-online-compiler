package com.slowlang.service;

import org.springframework.stereotype.Service;

@Service
public class CompilerService {
	
	public boolean compile(String sourceCode) {
		
		try {
			
			Thread.sleep(2000);
		
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		return true;
	}
}
