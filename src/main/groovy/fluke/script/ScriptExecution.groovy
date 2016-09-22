package fluke.script

import fluke.core.execution.Execution
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.KeywordMap
import fluke.core.keyword.Keywords
import fluke.docker.common.BuiltInFunctions

class ScriptExecution implements Execution, BuiltInFunctions {

	ExecutionContext executionContext = new ExecutionContext()
	
	def addToContext(name, val) {
		this.executionContext[name] = val
	}
	
	def disableBuilds() {
		this.executionContext.disableBuilds = true
	}
	
	def enableBuilds() {
		this.executionContext.disableBuilds = false
	}
	
	KeywordMap getKeywordMap() {
		return new KeywordMap(Keywords.FLUKE)
	}
}
