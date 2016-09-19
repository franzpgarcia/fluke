package fluke.operation

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.common.FlukeConsole;
import fluke.exception.OperationException;
import fluke.execution.ExecutionContext;

@Operation("from")
class FromOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()

	FromOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def from(String imageInput) {
		String[] imageSplit = imageInput.split(":")
		String image = imageSplit[0]
		String tag = imageSplit.size() > 1 ? imageSplit[1]:"latest"
		this.from([image: image, tag: tag])
	}

	@OperationMethod
	def from(Map<String, Object> imageMap) {
		Map imageContext = this.executionContext.variables.imageContext
		
		String[] imageSplit = imageMap.image.split(":")
		imageMap.image = imageSplit[0]
		if(imageMap.tag) {
			imageMap.tag = imageSplit.size() > 1 ? imageSplit[1]:"latest"
		}
		
		if(imageContext.currentImageId) {
			throw new OperationException("Duplicate `from` operation call in image \"${imageContext.image}\"")
		}

		console.printMessage "Pulling ${imageMap.image}:${imageMap.tag}"
		def pullResponse = dockerApi.pull(imageMap.image, imageMap.tag)
		imageContext.currentImageId = pullResponse.id
		console.printCommit imageContext.currentImageId
	}
}
