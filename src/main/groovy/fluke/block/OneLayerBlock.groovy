package fluke.block

import java.util.Map;

import fluke.annotation.AllowedOperations;
import fluke.annotation.Block;
import fluke.execution.ExecutionContext;

@Block(of="onelayer")
@AllowedOperations(["run"])
class OneLayerBlock implements ExecutableBlock {
	
	@Override
	def beforeExecute(ExecutionContext executionContext) {
		executionContext.variables.onelayer = [[]]
	}
	
	@Override
	def afterExecute(ExecutionContext executionContext, Map blockVars) {
		println(executionContext.variables.onelayer)
	}
}