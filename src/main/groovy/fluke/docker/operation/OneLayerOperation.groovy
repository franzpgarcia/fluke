package fluke.docker.operation

import fluke.core.common.FlukeConsole
import fluke.core.exception.NotImplementedYetException
import fluke.core.execution.ExecutionContext

//@Keyword("onelayer")
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
