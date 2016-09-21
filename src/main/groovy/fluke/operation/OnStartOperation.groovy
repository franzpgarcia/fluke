package fluke.operation;

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.common.FlukeConsole;
import fluke.execution.ExecutionContext;

@AllowedIn("image")
@Keyword("onstart")
public class OnStartOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	OnStartOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(Map onstart) {
		//TODO validation
		def imageContext = this.executionContext.variables["imageContext"] 
		imageContext.onstart = imageContext.onstart?:[:]
		imageContext.onstart << onstart
	}
}
