package fluke.block

import fluke.annotation.AllowedOperations;
import fluke.execution.ExecutionContext;

@AllowedOperations(["run", "install"])
class WithBlock implements ExecutableBlock {
	String user
	String directory
	
	@Override
	def beforeExecute() {
		if(user) {
			this.executionContext.variables["currentUser"] = user
		}
		if(directory) {
			this.executionContext.variables["currentDirectory"] = directory
		}
	}
	
}