package fluke.script

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

import fluke.operation.BuildOperation;

class ScriptRunner {
	def printErr = System.err.&println
	
	ScriptRunner() {
	}
	
	def runScript(String scriptStr, String build, Map args) {
		String scriptName = "FlukeScript" + Math.abs(new Random().nextInt());
		try {
			DelegatingScript script = (DelegatingScript) this.parseScript(scriptStr, scriptName, args)
			ScriptExecution scriptExecution = new ScriptExecution(outer: this)
			if(build) {
				scriptExecution.disableBuilds()
			}
			script.setDelegate(scriptExecution);
			script.run();
			if(build) {
				scriptExecution.enableBuilds()
				scriptExecution.executeOperation(BuildOperation.class, [[image: build]] as Object[])
			}
		} catch(Exception e) {
			this.handleExecutionException(e, scriptStr, scriptName)
		}
	}

	private void handleExecutionException(Exception e, String scriptStr, String scriptName) {
		StackTraceElement lineElement = e.stackTrace.find { it.className.contains scriptName }
		if(lineElement) {
			println ""
			int lineIssue = lineElement.lineNumber
			printErr "Error has occurred at line ${lineIssue}:"
			printErr "\t${e.message}\n"
			int startLine = Math.max(0, lineElement.lineNumber - 2)
			int finalLine = Math.min(lineElement.lineNumber + 4, scriptStr.split("\n").size() - 1)
			int curLine = startLine + 1
			scriptStr.split("\n")[startLine..finalLine].each {
				printErr "******** ${curLine}\t=>\t${it}"
				curLine++
			}
			printErr ""
		}
		System.exit(1)
	}
	
	private DelegatingScript parseScript(String scriptStr, String scriptName, Map args) {
		CompilerConfiguration cc = new CompilerConfiguration();
		cc.setScriptBaseClass(DelegatingScript.class.getName());
		Binding binding = new Binding()
		binding.setVariable("myargs", args)
		binding.setVariable("myenv", System.getenv())
		GroovyShell sh = new GroovyShell(this.class.getClassLoader(),binding,cc);
		try {
			return (DelegatingScript) sh.parse(scriptStr, scriptName)
		} catch(MultipleCompilationErrorsException e) {
			printErr e.message.replaceAll("${scriptName}:", "")
		} 
	}
	
}
