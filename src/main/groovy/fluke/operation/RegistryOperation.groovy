package fluke.operation

import fluke.annotation.Operation;
import fluke.common.FlukeConsole;
import fluke.execution.ExecutionContext;

@Operation("registry")
class RegistryOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	RegistryOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
}
