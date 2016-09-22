package fluke.operation;

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.common.FlukeConsole;
import fluke.execution.ExecutionContext;
import fluke.keyword.Keywords;

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
		def imageContext = this.executionContext.variables["imageContext"] 
		imageContext.onstart = [entrypoint: entrypoint, parameters: parameters]
	}
	
	def call(Map onstart) {
		this.call(onstart.entrypoint, onstart.parameters)
	}
}
