package fluke.operation

import fluke.annotation.Keyword;
import fluke.common.FlukeConsole;
import fluke.exception.NotImplementedYetException;
import fluke.execution.ExecutionContext;

@Keyword("registry")
class RegistryOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	RegistryOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call() {
		throw new NotImplementedYetException()
	}	
}
