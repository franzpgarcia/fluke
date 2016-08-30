package fluke.operation

import fluke.annotation.Operation;
import fluke.common.ConsoleOutputGenerator;
import fluke.execution.ExecutionContext;

@Operation("registry")
class RegistryOperation implements ConsoleOutputGenerator {
	private ExecutionContext executionContext
	
	RegistryOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
}
