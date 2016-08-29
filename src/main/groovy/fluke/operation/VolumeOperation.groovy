package fluke.operation

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@Operation("volume")
class VolumeOperation {
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	VolumeOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def volume(String... volumes) {
		this.volume(volumes as List)
	}
	
	@OperationMethod
	def volume(List<String> volumes) {
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand("ADDING VOLUME ${volumes}"), 
						   Volumes: volumes.collectEntries {[(it): [:]]}]
		
		println "Adding volumes ${volumes}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		println "=> ${imageContext.currentImageId}"
	}
	
}
