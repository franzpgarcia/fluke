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
	def build(Map<String, String> build) {
		String image = build["image"]
		if(image) {
			ImageBlock imageBlock = this.executionContext.images[image]
			if(imageBlock) {
				println "Building image ${image}"
				imageBlock.eval()
			} else {
				//TODO throw exception
			}
		}
	}
	
}
