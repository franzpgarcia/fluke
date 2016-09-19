package fluke.operation

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.block.WithBlock;
import fluke.common.FlukeConsole;
import fluke.exception.OperationException
import fluke.execution.ExecutionContext;

@Operation("with")
class WithOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	WithOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@OperationMethod
	def with(Map<String, String> useMap) {
		withUser(useMap.user)
		withDirectory(useMap.directory)
		withShell(useMap.shell)
	}
	
	@OperationMethod
	def with(Map<String, String> useMap, Closure closure) {
		WithBlock withBlock = new WithBlock(block: closure, user: useMap.user, directory: useMap.directory)
		withBlock.eval(this.executionContext)
	}
	
	private void withUser(String user) {
		if(user) {
			this.executionContext.variables["currentUser"] = user
		} 
	}
	
	private void withDirectory(String directory) {
		if(directory) {
			this.executionContext.variables["currentDirectory"] = directory
		}
	}
	
	private void withShell(String shell) {
		if(shell) {
			this.executionContext.variables["currentShell"] = shell
		}
	}
}