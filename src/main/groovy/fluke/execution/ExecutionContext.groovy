package fluke.execution

import fluke.block.ExecutableBlock

class ExecutionContext {
	Map variables = [:]
	Map procedures = [:]
	Map images = [:]
	Map registries = [:]
	ExecutableBlock currentBlock
	
	ExecutionContext copy() {
		new ExecutionContext(variables: this.variables.clone(),
						procedures: this.procedures.clone(),
						images: this.images.clone(),
						registries: this.registries.clone(),
						currentBlock: this.currentBlock)
	}
}

