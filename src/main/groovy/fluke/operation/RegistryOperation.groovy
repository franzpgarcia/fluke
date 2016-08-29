package fluke.operation

import fluke.annotation.Operation;
import fluke.execution.ExecutionContext;

@Operation("registry")
class RegistryOperation {
	private ExecutionContext executionContext
	
	RegistryOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
}
