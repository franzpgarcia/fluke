package fluke.docker.block

import java.util.Map;

import fluke.core.annotation.Block;
import fluke.core.block.ExecutableBlock;
import fluke.core.execution.ExecutionContext;

//@Block(of="onelayer")
class OneLayerBlock implements ExecutableBlock {
	
	@Override
	def beforeExecute(ExecutionContext executionContext) {
		executionContext.onelayer = [[]]
	}
	
	@Override
	def afterExecute(ExecutionContext executionContext, Map blockVars) {
		println(executionContext.onelayer)
	}
}