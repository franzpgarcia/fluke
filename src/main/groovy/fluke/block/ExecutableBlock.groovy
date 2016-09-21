package fluke.block

import groovy.lang.Closure;

import java.util.LinkedHashMap;

import fluke.annotation.Block;
import fluke.exception.InvalidCallException
import fluke.execution.BlockExecution
import fluke.execution.ExecutionContext;
import fluke.keyword.KeywordMap;

trait ExecutableBlock {
	Closure block
	
	def call(ExecutionContext executionContext) {
		def clone = block.clone()
		beforeExecute(executionContext)
		
		def executionContextCopy = executionContext.copy()
		executionContextCopy.currentBlock = this
		
		String blockOf = getBlockOf()
		clone.delegate = new BlockExecution(outer: this, 
											blockName: blockOf,
											executionContext: executionContextCopy, 
											keywordMap: new KeywordMap(blockOf))
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
}