package fluke.operation

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.common.ConsoleOutputGenerator;
import fluke.exception.OperationException;
import fluke.execution.ExecutionContext;

@Operation("from")
class FromOperation implements ConsoleOutputGenerator {
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()

	FromOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def from(String image) {
		this.from([image: image, tag: "latest"])
	}

	@OperationMethod
	def from(Map<String, Object> imageMap) {
		Map imageContext = this.executionContext.variables["imageContext"]
		imageMap.tag = imageMap.tag?:"latest"

		if(imageContext.currentImageId) {
			throw new OperationException("Duplicate `from` operation call in image \"${imageContext.image}\"")
		}

		printMessage "Pulling ${imageMap.image}:${imageMap.tag}"
		def pullResponse = dockerApi.pull(imageMap.image, imageMap.tag)
		imageContext.currentImageId = pullResponse.id
		printCommit imageContext.currentImageId
	}
}
