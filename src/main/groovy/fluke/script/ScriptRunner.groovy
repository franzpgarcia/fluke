package fluke.script

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

import fluke.operation.BuildOperation;

class ScriptRunner {
		
	ScriptRunner() {
	}
	
	def runScript(String scriptStr, String build, Map args) {
		String scriptName = "FlukeScript" + Math.abs(new Random().nextInt());
		CompilerConfiguration cc = new CompilerConfiguration();
		cc.setScriptBaseClass(DelegatingScript.class.getName());
		Binding binding = new Binding()
		binding.setVariable("myargs", args)
		binding.setVariable("myenv", System.getenv())
		GroovyShell sh = new GroovyShell(this.class.getClassLoader(),binding,cc);
		try {
			DelegatingScript script = (DelegatingScript)sh.parse(scriptStr, scriptName)
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
		} catch(MultipleCompilationErrorsException e) {
			throw e
		} catch(Exception e) {
			StackTraceElement lineElement = e.stackTrace.find { it.className.contains scriptName }
			if(lineElement) {
				println "Issue at line: ${lineElement.lineNumber}"
				println scriptStr.split("\n")[lineElement.lineNumber - 1]
			}
			throw e
		}
	}

}
