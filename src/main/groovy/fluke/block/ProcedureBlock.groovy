package fluke.block

import fluke.annotation.AllowedOperations;
import fluke.annotation.Block;
import fluke.execution.ExecutionContext;

@Block(of="procedure")
@AllowedOperations(["run", "port", "install", "copy", "volume", "with", "setenv"])
class ProcedureBlock implements ExecutableBlock {
	
}