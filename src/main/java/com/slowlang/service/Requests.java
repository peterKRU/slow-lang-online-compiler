package com.slowlang.service;

import java.util.HashSet;
import java.util.Set;

public class Requests {

	public static Set<String> types;

	public static final String COMPILE = "compile";
	public static final String RUN = "run";
	public static final String COMPILE_AND_RUN = "compile&run";
	public static final String HELP = "help";

	static {
		types = new HashSet<String>();
		types.add(COMPILE);
		types.add(RUN);
		types.add(COMPILE_AND_RUN);
		types.add(HELP);
	}
}
