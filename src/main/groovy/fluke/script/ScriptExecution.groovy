package fluke.script

import fluke.common.BuiltInFunctions;
import fluke.execution.Execution;
import fluke.execution.ExecutionContext;
import fluke.keyword.KeywordMap;
import fluke.keyword.Keywords;

class ScriptExecution implements Execution, BuiltInFunctions {

	ExecutionContext executionContext = new ExecutionContext()
	
	def addToContext(name, val) {
		this.executionContext.variables[name] = val
	}
	
	def disableBuilds() {
		this.executionContext.variables.disableBuilds = true
	}
	
	def enableBuilds() {
		this.executionContext.variables.disableBuilds = false
	}
	
	KeywordMap getKeywordMap() {
		return new KeywordMap(Keywords.FLUKE)
	}
}
