package fluke.docker.operation

import fluke.core.annotation.AllowedIn
import fluke.core.annotation.Keyword
import fluke.core.common.FlukeConsole
import fluke.core.exception.OperationException
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.Keywords
import fluke.docker.api.DockerApi
import fluke.docker.block.ImageBlock

@AllowedIn(Keywords.FLUKE)
@Keyword(Keywords.BUILD)
class BuildOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	BuildOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call(Map build) {
		String imageInput = build.image
		if(!this.executionContext.disableBuilds) {
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
				this.executionContext.imageContext = [image: image,
										  buildNumber: Math.abs(new Random().nextInt()),
										  tag: tag]
				checkPreviousImage(image, tag)
				imageBlock(this.executionContext)
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
		boolean force = this.executionContext.force

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
