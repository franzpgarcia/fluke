package fluke.execution

class ExecutionContext {
	Map variables = [:]
	Map procedures = [:]
	Map images = [:]
	Map registries = [:]
	
	ExecutionContext copy() {
		new ExecutionContext(variables: this.variables.clone(),
						procedures: this.procedures.clone(),
						images: this.images.clone(),
						registries: this.registries.clone())
	}
}

