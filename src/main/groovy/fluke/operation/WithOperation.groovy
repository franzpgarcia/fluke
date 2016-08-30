package fluke.operation

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.block.WithBlock;
import fluke.execution.ExecutionContext;

@Operation("with")
class WithOperation {

	private ExecutionContext executionContext
	
	WithOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@OperationMethod
	def with(Map<String, String> useMap) {
		withUser(useMap.user)
		withDirectory(useMap.directory)
	}
	
	@OperationMethod
	def with(Map<String, String> useMap, Closure closure) {
		WithBlock withBlock = new WithBlock(block: closure, user: useMap.user, directory: useMap.directory)
		withBlock.eval(this.executionContext)
	}
	
	private void withUser(String user) {
		if(user) {
			this.executionContext["currentUser"] = user
		} 
	}
	
	private void withDirectory(String directory) {
		if(directory) {
			this.executionContext["currentDirectory"] = directory
		}
	}
}
