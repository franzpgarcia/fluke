package fluke.operation;

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.block.ImageBlock;
import fluke.execution.ExecutionContext;

@Operation("build")
class BuildOperation {
	
	private ExecutionContext executionContext
	
	BuildOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def build(Map build) {
		Map variables = this.executionContext.variables
		String image = build["image"]
		if(image && !variables.disableBuilds) {
			ImageBlock imageBlock = this.executionContext.images[image]
			if(imageBlock) {
				println "Building image ${image}"
				imageBlock.eval()
				println "Build ${image} completed successfully"
			} else {
				println "Image ${image} not found"
				//TODO throw exception
			}
		}
	}
	
}
