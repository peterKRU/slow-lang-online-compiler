package com.slowlang.service;

import org.springframework.stereotype.Service;

import compiler.Compiler;

@Service
public class CompilerService {
	
	public byte[] compile(String sourceCode) {
		
		Compiler compiler = new Compiler();
		
		return compiler.compile(sourceCode, Constants.DEFAULT_PROGRAM_NAME);
	}
}
