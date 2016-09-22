package fluke.docker.block

import fluke.core.block.ExecutableBlock
import fluke.core.execution.ExecutionContext

//@Block(of="onelayer")
class OneLayerBlock extends ExecutableBlock {
	
	@Override
	def beforeExecute(ExecutionContext executionContext) {
		executionContext.onelayer = [[]]
	}
	
	@Override
	def afterExecute(ExecutionContext executionContext, Map blockVars) {
		println(executionContext.onelayer)
	}
}