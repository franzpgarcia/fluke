package fluke.script

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;

class ScriptRunner {
		
	ScriptRunner() {
	}
	
	def runScript(String scriptStr, Map args) {
		String scriptName = "FlukeScript" + Math.abs(new Random().nextInt());
		CompilerConfiguration cc = new CompilerConfiguration();
		cc.setScriptBaseClass(DelegatingScript.class.getName());
		Binding binding = new Binding()
		binding.setVariable("myargs", args)
		binding.setVariable("myenv", System.getenv())
		GroovyShell sh = new GroovyShell(this.class.getClassLoader(),binding,cc);
		try {
			DelegatingScript script = (DelegatingScript)sh.parse(scriptStr, scriptName)
			script.setDelegate(new ScriptExecution(outer: this));
			script.run();
		} catch(MultipleCompilationErrorsException e) {
			throw e
		} catch(Exception e) {
			StackTraceElement lineElement = e.stackTrace.find { it.className.contains scriptName }
			println "Issue at line: ${lineElement.lineNumber}"
			println scriptStr.split("\n")[lineElement.lineNumber - 1]
			throw e
		}
	}

}
