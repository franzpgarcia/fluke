package fluke.core.block

import groovy.lang.Closure;

import java.util.LinkedHashMap;

import fluke.core.annotation.Block;
import fluke.core.exception.InvalidCallException;
import fluke.core.execution.BlockExecution;
import fluke.core.execution.ExecutionContext;
import fluke.core.keyword.KeywordMap;
import fluke.definition.Definition;

trait ExecutableBlock extends Definition {
	
	def call(ExecutionContext executionContext) {
		def clone = this.closure.clone()
		beforeExecute(executionContext)
		
		def executionContextCopy = executionContext.copy(this)
		
		clone.delegate = new BlockExecution(outer: this, 
											executionContext: executionContextCopy, 
											keywordMap: new KeywordMap(getBlockOf()))
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