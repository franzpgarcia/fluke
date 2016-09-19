package fluke.script

import fluke.annotation.AllowedOperations;
import fluke.common.BuiltInFunctions;
import fluke.execution.Execution;
import fluke.execution.ExecutionContext;
import fluke.operation.map.OperationMap;

@AllowedOperations(["build", "image", "registry", "procedure"])
class ScriptExecution implements Execution, BuiltInFunctions {

	ExecutionContext executionContext = new ExecutionContext()
	OperationMap operationMap = {
		new OperationMap(null, owner.getClass().getAnnotation(AllowedOperations.class).value())
	}()
	
	def addToContext(name, val) {
		this.executionContext.variables[name] = val
	}
	
	def disableBuilds() {
		this.executionContext.variables.disableBuilds = true
	}
	
	def enableBuilds() {
		this.executionContext.variables.disableBuilds = false
	}
}
