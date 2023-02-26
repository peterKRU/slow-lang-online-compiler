
# SlowLang Online Compiler

<p align="center">
  <a href="#features">Features</a> •
  <a href="#how-to-use">How To Use</a> •
  <a href="#related-projects">Related Projects</a> •
  <a href="#tools-&-software">Tools & Software</a> •
  <a href="#license">License</a>
</p>

![slowlang-logo]

***
SlowLang is a class-based, object-oriented programming language designed for modularity and ease of extensibility. 
Its primary purpose is to serve as a robust foundation for developing domain-specific languages with more complex syntax 
and features. 

## Features

The SlowLang Online Compiler application provides the following features: 

* **Compiler**
Compiles SlowLang programs into executable files and generates parse trees.

* **Run-time Environment**
Provides a run-time environment that allows users to run their compiled SlowLang programs directly in the browser.

* **Code Editor**
Built with Ace, a modern and feature-rich code editor.

* **Chat Bot**
Forwards user requests to the SlowLang compiler instance. Allows users to interact with the compiler in a conversational way, making it easier to understand and use the application.
The chat bot displays server responses as messages, providing users with feedback on their requests and the status of the compiler.

* **Syntax Explanation**
Explains the syntax of the SlowLang code written by the user. This feature helps users better understand the language and identify errors in their code.

* **Parse Tree Diagram**
Displays a diagram of the parse tree generated from the user's source code. This diagram provides users with a visual representation of the structure of their code and helps them understand how their code is parsed and executed.

##### [Back to top](#table-of-contents)

## How To Use

Visit the application's web page: https://slowlang-online-compiler.com/compiler

> **Warning**
> The application is not yet deployed. The link is not functional. 

The Compiler's main page contains two major sections: **Code Editor** (left) and **Chat Bot** (ritht). 
![app-screenshot-1]

### 1. Code Editor Section
This section contains an Ace code editor, where the user can write SlowLang source code.
Above the editor window there is a menu bar featuring the following commands: 

| Command | Functionality |
| --- | --- | 
| Compile&Run | Sends a 'compile' request to the Compiler instance. If the compilation is successful, a 'run' request is sent to the Execution Engine instance. Once the process is terminated, the SlowLang ChatBot displays the process log in the chat section. |
| Generate Parse Tree | Sends a 'generate parse tree' request to the Compiler instance. If compiled successfully, the output is displayed in the chat section in .jpg format. |
| Debug | Sends a 'debug' request to the Compiler instance. The Compiler generates an Error Log. The output is displayed in the chat section. |
| Test | Generates a simple random SlowLang program source code, than sends a "compile&run" request to the server. The ChatBot displays a parse tree and a process log in the chat section. | 
| Explain Syntax | Sends a 'analize syntax' request to the Compiler instance. A detailed syntax analysis is displayed in the chat section. | 

---

### 1. Chat Bot Section

This section contains a chat area, where the User interacts with the SlowLang chat bot.
When a new session is started, the chat bot sends a default 'welcome' message with basic instructions for interactions.  The user can either type a command frow a list of available commands, or use the menu bar above the chat area. The menu bar features the following commands: 

| Command | Functionality |
| --- | --- | 
| Display Available Commands | The chat bot displays a full list of avalilable commands and brief overview of their functionality. |
| Generate Verbose Log | Sends a 'generate log' command with a '--verbose' flag to the Execution Engine service. A verbose log contains all available metadata about a terminated process, such as heap dumps, states of the call stack etc. Note: This command is mainly used for functionality testing and will be removed in future versions. |
| Configure | The chat bot generates a list of available configurations for the Execution Engine service, along with instruction on how to change values. The User can configure values, such as min/max heap size. |
| Clear | Clears the current chat area. |

---

The User can type the 'help' command for additional instructions. 
That's pretty much it, have fun!

##### [Back to top](#table-of-contents)

## Related Projects

 * **SlowLang Compiler:** A 'front-end' compiler for a general-purpose, class-based, object-oriented language called SlowLang. The program is written in java and features an Antlr4 lexer and parser.   
  GitHub repository: [slow-lang-compiler](https://github.com/peterKRU/slow-lang-compiler)

* **SlowLang Execution Engine:** An execution engine for SlowLang. Executes programs compiled by the SlowLang Compiler. Features a class loader, memory manager, heap, garbage collector and a stack-based machine. 
 GitHub repository: [slow-lang-execution-engine](https://github.com/peterKRU/slow-lang-execution-engine)

* **Runner:** A terminal client application which uses a Compiler and an Execution Engine instances to compile and run SlowLang code. 
GitHub repository: [slow-lang-runner](https://github.com/peterKRU/slow-lang-runner)

##### [Back to top](#table-of-contents)

## Tools & Software

**Frameworks:**
* Spring
* Vaadin

**CI tools:**
* Maven
* Jenkins
* Docker


**Other tools:**
* Ace Code Editor

##### [Back to top](#table-of-contents)

## Licence

MIT License

##### [Back to top](#table-of-contents)

***



<!-- MARKDOWN LINKS & IMAGES -->

[slowlang-logo]: https://raw.githubusercontent.com/peterKRU/slow-lang-compiler/master/docs/SlowLang_2.jpg "SlowLang Logo"

[under-construction]: https://raw.githubusercontent.com/peterKRU/slow-lang-compiler/master/docs/Under-Construction-300x222.png "Under Construction"

[app-screenshot-1]: https://raw.githubusercontent.com/peterKRU/slow-lang-online-compiler/master/docs/app_screenshot2.png "Application Main Menu"
