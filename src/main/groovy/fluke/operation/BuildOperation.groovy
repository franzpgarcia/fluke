package fluke.operation;

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.block.ImageBlock;
import fluke.exception.OperationException;
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
				println "------ Building image ${image} -------"
				try {
					imageBlock.eval(this.executionContext)
					println "Build of image ${image} completed successfully"
				} catch(Exception e) {
					println "Build of image ${image} failed"
					throw e
				}
			} else {
				throw new OperationException("Image ${image} not defined")
			}
		}
	}
	
}
