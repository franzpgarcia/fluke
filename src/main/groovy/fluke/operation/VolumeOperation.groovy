package fluke.operation

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.api.DockerApi;
import fluke.common.FlukeConsole;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@AllowedIn(["image", "procedure", "with"])
@Keyword("volume")
class VolumeOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	VolumeOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call(String... volumes) {
		this.call(volumes as List)
	}
	
	def call(List<String> volumes) {
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand("ADDING VOLUME ${volumes}"), 
						   Volumes: volumes.collectEntries {[(it): [:]]}]
		
		console.printMessage "Adding volumes ${volumes}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
	
}
