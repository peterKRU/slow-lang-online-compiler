package com.slowlang.views;

public class UIConstants {
	
	public static final String DEFAULT_HELP_MESSAGE = "Welcome to the SlowLang Compiler Bot! You can use the following commands to interact with me:\n"
			+ "\n"
			+ "- compile&run: compile and run your code\n"
			+ "- generate parse tree: compile your code and generate a parse tree\n"
			+ "- test: compile and run default piece of source code\n"
			+ "- debug: ask the compiler to help you with any compile-time errors while writing code\n"
			+ "\n"
			+ "Alternatively, you can use the \"Compile&Run\" button to run your code. If you want to use a command, just type it in the message field and I'll take care of the rest! If you ever need help again, just type \"help\" and I'll give you a reminder of the available commands.";
	
	public static final String DEFAULT_CODE_INPUT = "main {\n"
			+ "    \n"
			+ "    x = 1;\n"
			+ "    y = 2; \n"
			+ "    \n"
			+ "    sum = add(x, y);\n"
			+ "    print(sum);\n"
			+ "}\n"
			+ "\n"
			+ "class ExampleClass {\n"
			+ "    \n"
			+ "    int add(int first, int second){\n"
			+ "        \n"
			+ "        result = first + second;\n"
			+ "        \n"
			+ "        return result;\n"
			+ "    }\n"
			+ "}";
}
