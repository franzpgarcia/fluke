package fluke.docker.operation

import fluke.core.common.FlukeConsole
import fluke.core.exception.NotImplementedYetException
import fluke.core.execution.ExecutionContext

//@Keyword("registry")
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
