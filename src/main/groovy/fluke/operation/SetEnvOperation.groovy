package fluke.operation

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.api.DockerApi;
import fluke.common.FlukeConsole;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@AllowedIn(["image", "procedure", "with"])
@Keyword("setenv")
class SetEnvOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	SetEnvOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call(Map<String, String> envs) {
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand(this.executionContext, "ADDING ENVIRONMENT VARIABLES ${envs}"),
							Env: envs.collect {k, v -> "${k}=${v}"}]
		
		console.printMessage "Setting environment variables ${envs}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		Map commitQuery =  HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
}
