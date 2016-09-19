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
		executionContextCopy.currentBlock = this
		
		String blockOf = getBlockOf()
		clone.delegate = new BlockExecution(outer: this, 
											blockName: blockOf,
											executionContext: executionContextCopy, 
											operationMap: buildOperationMap(blockOf))
		clone.resolveStrategy = Closure.DELEGATE_FIRST
		try {
			clone()
			afterExecute(executionContext, clone.binding.variables)
		} catch(Exception e) {
			onError(e, executionContextCopy)
		}
	}
	
	String getBlockOf() {
		return this.getClass().getAnnotation(Block.class)?.of()
	}
	
	boolean isBlockOf(String of) {
		return of == getBlockOf()
	}
	
	def beforeExecute(ExecutionContext executionContext) {
		
	}
	
	def onError(Exception e, ExecutionContext executionContext) {
		throw e
	}
	
	def afterExecute(ExecutionContext executionContext, Map blockVars) {
		
	}
	
	private OperationMap buildOperationMap(String blockName) {
		AllowedOperations annotation = this.getClass().getAnnotation(AllowedOperations.class)
		return new OperationMap(blockName, annotation.value())
	}
}