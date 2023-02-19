package com.slowlang.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotService {
	
	@Autowired
	private CompilerService compilerService;
	
	@Autowired
	private ExecutionEngineService executionEngineService;
	
	public String handleUserMessageRequest(String userMessage) {
		
		return null;
	}
	
	public String handleRequest(Request request) {
		
		if(request.getType().equals(Requests.COMPILE_AND_RUN)) {
			
			String sourceCode = request.getBody();
			byte[] compiled = compilerService.compile(sourceCode);
			String processLog = executionEngineService.execute(compiled);
			
			return generateResponse(processLog); 
		}
		
		return generateResponse(Messages.INVALID_REQUEST);
	}
	
	private String generateResponse(String response) {
		
		return response;
	}
}
