package fluke.operation

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.common.ConsoleOutputGenerator;
import fluke.exception.NotImplementedYetException;
import fluke.execution.ExecutionContext;

@Operation("onelayer")
class OneLayerOperation implements ConsoleOutputGenerator {
	private ExecutionContext executionContext
	
	OneLayerOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@OperationMethod
	def onelayer(Closure closure) {
		throw new NotImplementedYetException()
	}
}
