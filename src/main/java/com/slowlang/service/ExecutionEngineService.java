package com.slowlang.service;

import java.util.List;

import org.springframework.stereotype.Service;

import execution_engine.ChainedVM;
import execution_engine.MemoryManager;

@Service
public class ExecutionEngineService {

	public String execute(byte[] programBytes) {

		MemoryManager memoryManager = new MemoryManager(programBytes, Constants.DEFAULT_PROGRAM_NAME);
		ChainedVM vm = new ChainedVM(memoryManager);
		vm.executeProgram();

		List<String> log = ChainedVM.logger.getLog();

		return log.toString();
	}

}
