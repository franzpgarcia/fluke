package fluke.operation;

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.common.FlukeConsole;
import fluke.execution.ExecutionContext;

@Operation("onstart")
public class OnStartOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	OnStartOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@OperationMethod
	def onstart(Map onstart) {
		//TODO validation
		def imageContext = this.executionContext.variables["imageContext"] 
		imageContext.onstart = imageContext.onstart?:[:]
		imageContext.onstart << onstart
	}
}
