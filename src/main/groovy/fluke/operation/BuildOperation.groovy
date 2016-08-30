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
		String imageInput = build["image"]
		if(!variables.disableBuilds) {
			if(imageInput) {
				String[] imageSplit = imageInput.split(":")
				String image = imageSplit[0]
				String tag = imageSplit.size() > 1 ? imageSplit[1]:null
				this.buildImage(image, tag)
			}
		}
	}
	
	def buildImage(String image, String tag) {		
		ImageBlock imageBlock = this.executionContext.images[image]
		if(imageBlock) {
			printMessage "\n------ Building image ${image}:${tag} -------"
			try {
				Map variables = this.executionContext.variables
				variables.imageContext = [image: image,
										  buildNumber: Math.abs(new Random().nextInt()),
										  tag: tag?:"latest"]
				imageBlock.eval(this.executionContext)
				printMessage "Build of image ${image}:${tag} completed successfully\n"
			} catch(Exception e) {
				printMessage "Build of image ${image}:${tag} failed\n"
				throw e
			}
		} else {
			throw new OperationException("Image ${image} not defined")
		}
	}
	
}
