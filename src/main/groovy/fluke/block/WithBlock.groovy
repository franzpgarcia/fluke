package fluke.block

import fluke.annotation.AllowedOperations;
import fluke.annotation.Block;
import fluke.execution.ExecutionContext;

@Block(of="with")
@AllowedOperations(["run", "install", "copy"])
class WithBlock implements ExecutableBlock {
	String user
	String directory
	
	@Override
	def beforeExecute(ExecutionContext executionContext) {
		if(user) {
			executionContext.variables["currentUser"] = user
		}
		if(directory) {
			executionContext.variables["currentDirectory"] = directory
		}
	}
	
}