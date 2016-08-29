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
		new OperationMap(owner.getClass().getAnnotation(AllowedOperations.class).value())
	}()
}
