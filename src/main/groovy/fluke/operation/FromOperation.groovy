package fluke.operation

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.api.DockerApi;
import fluke.common.FlukeConsole;
import fluke.exception.OperationException;
import fluke.execution.ExecutionContext;

@AllowedIn("image")
@Keyword("from")
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
		Map imageContext = this.executionContext.variables.imageContext
		
		if(imageContext.currentImageId) {
			throw new OperationException("Duplicate `from` operation call in image \"${imageContext.image}\"")
		}

		console.printMessage "Pulling ${imageMap.image}:${imageMap.tag}"
		def pullResponse = dockerApi.pull(imageMap.image, imageMap.tag)
		imageContext.currentImageId = pullResponse.id
		console.printCommit imageContext.currentImageId
	}
}
