package fluke.block

import fluke.annotation.Block;
import fluke.execution.ExecutionContext;
import fluke.keyword.Keywords;

@Block(of=Keywords.WITH)
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