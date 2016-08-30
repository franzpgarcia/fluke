package fluke.operation;

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.block.ImageBlock;
import fluke.common.ConsoleOutputGenerator;
import fluke.exception.OperationException;
import fluke.execution.ExecutionContext;

@Operation("build")
class BuildOperation implements ConsoleOutputGenerator {
	
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
				printMessage "\n------ Building image ${image} -------"
				try {
					imageBlock.eval(this.executionContext)
					printMessage "Build of image ${image} completed successfully\n"
				} catch(Exception e) {
					printMessage "Build of image ${image} failed\n"
					throw e
				}
			} else {
				throw new OperationException("Image ${image} not defined")
			}
		}
	}
	
}
