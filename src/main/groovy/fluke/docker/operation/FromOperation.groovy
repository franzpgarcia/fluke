package fluke.docker.operation

import fluke.core.annotation.AllowedIn
import fluke.core.annotation.Keyword
import fluke.core.common.FlukeConsole
import fluke.core.exception.OperationException
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.Keywords
import fluke.docker.api.DockerApi

@AllowedIn(Keywords.IMAGE)
@Keyword(Keywords.FROM)
class FromOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()

	FromOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call(String imageInput) {
		String[] imageSplit = imageInput.split(":")
		String image = imageSplit[0]
		String tag = imageSplit.size() > 1 ? imageSplit[1]:"latest"
		this.call([image: image, tag: tag])
	}

	def call(Map<String, Object> imageMap) {
		Map imageContext = this.executionContext.imageContext
		
		if(imageContext.currentImageId) {
			throw new OperationException("Duplicate `from` operation call in image \"${imageContext.image}\"")
		}

		console.printMessage "Pulling ${imageMap.image}:${imageMap.tag}"
		def pullResponse = dockerApi.pull(imageMap.image, imageMap.tag)
		imageContext.currentImageId = pullResponse.id
		imageContext.fromImageId = pullResponse.id
		console.printCommit imageContext.currentImageId
	}
}
