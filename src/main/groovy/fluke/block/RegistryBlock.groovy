package fluke.block;

import fluke.annotation.AllowedOperations;
import fluke.annotation.Block;
import fluke.execution.ExecutionContext;

@Block(of="registry")
@AllowedOperations([])
class RegistryBlock implements ExecutableBlock {
	
}
