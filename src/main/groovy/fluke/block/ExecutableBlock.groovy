package fluke.block

import groovy.lang.Closure;

import java.util.LinkedHashMap;

import fluke.annotation.Block;
import fluke.annotation.AllowedOperations;
import fluke.exception.InvalidOperationCallException
import fluke.execution.BlockExecution
import fluke.execution.ExecutionContext;
import fluke.operation.map.OperationMap;

trait ExecutableBlock {
	Closure block
	
	def eval(ExecutionContext executionContext) {
		def clone = block.clone()
		beforeExecute(executionContext)
		def executionContextCopy = executionContext.copy()
		String blockName = this.getClass().getAnnotation(Block.class)?.of()
		clone.delegate = new BlockExecution(outer: this, 
											blockName: blockName,
											executionContext: executionContextCopy, 
											operationMap: buildOperationMap(blockName))
		clone.resolveStrategy = Closure.DELEGATE_FIRST
		clone()
		afterExecute(executionContext, clone.binding.variables)
	}
	
	def beforeExecute(ExecutionContext executionContext) {
		
	}
	
	def afterExecute(ExecutionContext executionContext, Map blockVars) {
		
	}
	
	private OperationMap buildOperationMap(String blockName) {
		AllowedOperations annotation = this.getClass().getAnnotation(AllowedOperations.class)
		return new OperationMap(blockName, annotation.value())
	}
}