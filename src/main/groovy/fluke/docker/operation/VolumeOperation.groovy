package fluke.docker.operation

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.common.FlukeConsole;
import fluke.core.execution.ExecutionContext;
import fluke.core.keyword.Keywords;
import fluke.docker.api.DockerApi;
import fluke.docker.common.HelperFunctions;

@AllowedIn([Keywords.IMAGE, Keywords.PROCEDURE, Keywords.WITH])
@Keyword(Keywords.VOLUME)
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
		Map imageContext = this.executionContext.imageContext
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
