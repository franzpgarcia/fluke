package fluke.operation

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.common.ConsoleOutputGenerator;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@Operation("port")
class PortOperation implements ConsoleOutputGenerator {
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()

	PortOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def port(int port) {
		this.port(port, "tcp")
	}

	@OperationMethod
	def port(int port, String protocol) {
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand("EXPOSING ${port} ${protocol}"),
							ExposedPorts: ["${port}/${protocol}": [:]]]
		
		printMessage "Exposing port ${port}/${protocol}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		printCommit imageContext.currentImageId
	}
}
