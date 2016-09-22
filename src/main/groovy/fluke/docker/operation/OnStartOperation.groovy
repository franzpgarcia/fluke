package fluke.docker.operation;

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.common.FlukeConsole;
import fluke.core.execution.ExecutionContext;
import fluke.core.keyword.Keywords;

@AllowedIn(Keywords.IMAGE)
@Keyword(Keywords.ONSTART)
public class OnStartOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	OnStartOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(String entrypoint, String... parameters) {
		//TODO validation
		def imageContext = this.executionContext.imageContext
		imageContext.onstart = [entrypoint: entrypoint, parameters: parameters]
	}
	
	def call(Map onstart) {
		this.call(onstart.entrypoint, onstart.parameters)
	}
}
