package fluke.operation

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.common.ConsoleOutputGenerator;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@Operation("setenv")
class SetEnvOperation implements ConsoleOutputGenerator {
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	SetEnvOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def setenv(Map<String, String> envs) {
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand("ADDING ENVIRONMENT VARIABLES ${envs}"),
							Env: envs.collect {k, v -> "${k}=${v}"}]
		
		printMessage "Setting environment variables ${envs}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		Map commitQuery =  HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		printCommit imageContext.currentImageId
	}
}
