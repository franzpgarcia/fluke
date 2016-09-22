package fluke.docker.block

import fluke.core.annotation.Block
import fluke.core.block.ExecutableBlock
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.Keywords

@Block(of=Keywords.WITH)
class WithBlock extends ExecutableBlock {
	String user
	String directory
	
	@Override
	def beforeExecute(ExecutionContext executionContext) {
		if(user) {
			executionContext.currentUser = user
		}
		if(directory) {
			executionContext.currentDirectory = directory
		}
	}
	
}