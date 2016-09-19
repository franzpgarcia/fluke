package fluke.operation;

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.block.ImageBlock;
import fluke.common.FlukeConsole;
import fluke.exception.OperationException;
import fluke.execution.ExecutionContext;

@Operation("build")
class BuildOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	BuildOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def build(Map build) {
		Map variables = this.executionContext.variables
		String imageInput = build.image
		if(!variables.disableBuilds) {
			if(imageInput) {
				String[] imageSplit = imageInput.split(":")
				String image = imageSplit[0]
				String tag = imageSplit.size() > 1 ? imageSplit[1]:"latest"
				if(build.tag) {
					tag = build.tag
				}
				this.buildImage(image, tag)
			}
		}
	}
	
	def buildImage(String image, String tag) {		
		ImageBlock imageBlock = this.executionContext.images[image]
		if(imageBlock) {
			console.printMessage "\n------ Building image ${image}:${tag} -------"
			try {
				Map variables = this.executionContext.variables
				variables.imageContext = [image: image,
										  buildNumber: Math.abs(new Random().nextInt()),
										  tag: tag]
				checkPreviousImage(image, tag)
				imageBlock.eval(this.executionContext)
				console.printMessage "Build of image ${image}:${tag} completed successfully\n"
			} catch(Exception e) {
				console.printMessage "Build of image ${image}:${tag} failed\n"
				throw e
			}
		} else {
			throw new OperationException("Image ${image} not defined")
		}
	}

	private checkPreviousImage(String image, String tag) {
		boolean force = this.executionContext.variables.force

		String prevImageId = this.dockerApi.image("${image}:${tag}")?.Id
		if(prevImageId) {
			if(force) {
				this.dockerApi.rmi(prevImageId)
			} else {
				throw new OperationException("Image ${image}:${tag} already exists")
			}
		}
	}
	
}
