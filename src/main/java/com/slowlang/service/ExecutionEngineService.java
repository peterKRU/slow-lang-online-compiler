package com.slowlang.service;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

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
	
	public ListenableFuture<String> executeAsync(byte[] bytecode) {
		
		try {
			
			Thread.sleep(2000);
		
		} catch (InterruptedException e) {
			
			return 	AsyncResult.forExecutionException(new RuntimeException("Error"));
		}
		
		return AsyncResult.forValue("Mockup async response");
	}	
	
}
