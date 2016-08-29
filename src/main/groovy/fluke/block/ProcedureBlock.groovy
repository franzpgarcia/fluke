package fluke.block

import fluke.annotation.AllowedOperations;
import fluke.execution.ExecutionContext;

@AllowedOperations(["run", "port", "install", "copy", "volume", "with", "setenv"])
class ProcedureBlock implements ExecutableBlock {
	
}