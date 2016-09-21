package fluke.operation

import fluke.annotation.Keyword;
import fluke.common.FlukeConsole;
import fluke.exception.NotImplementedYetException;
import fluke.execution.ExecutionContext;

@Keyword("onelayer")
class OneLayerOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	OneLayerOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(Closure closure) {
		throw new NotImplementedYetException()
	}
}
