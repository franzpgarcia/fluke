package fluke.block

import groovy.lang.Closure;

import java.util.LinkedHashMap;

import fluke.annotation.AllowedOperations;
import fluke.exception.InvalidOperationCallException
import fluke.execution.BlockExecution
import fluke.execution.ExecutionContext;
import fluke.operation.map.OperationMap;

trait ExecutableBlock {
	Closure block
	ExecutionContext executionContext
	
	def eval() {
		def clone = block.clone()
		beforeExecute()
		def executionContextCopy = this.executionContext.copy()
		clone.delegate = new BlockExecution(outer: this, executionContext: executionContextCopy, operationMap: buildOperationMap())
		clone.resolveStrategy = Closure.DELEGATE_FIRST
		clone()
		afterExecute(clone.binding.variables)
	}
	
	def beforeExecute() {
		
	}
	
	def afterExecute(Map blockVars) {
		
	}
	
	private OperationMap buildOperationMap() {
		AllowedOperations annotation = this.getClass().getAnnotation(AllowedOperations.class)
		return new OperationMap(annotation.value())
	}
}