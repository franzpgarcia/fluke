package fluke.docker.operation

import fluke.core.annotation.AllowedIn
import fluke.core.annotation.Keyword
import fluke.core.common.FlukeConsole
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.Keywords
import fluke.docker.api.DockerApi
import fluke.docker.common.HelperFunctions

@AllowedIn([Keywords.IMAGE, Keywords.PROCEDURE, Keywords.WITH])
@Keyword(Keywords.RUN)
class RunOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	RunOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(String... args) {
		this.runNow(args)
	}
	
	def call(List<String> args) {
		this.call(*args)
	}
	
	def runNow(String... args) {
		Map imageContext = this.executionContext.imageContext
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: args]
		
		console.printMessage "Running command ${args}"
		def runResponse = dockerApi.run(containerConfig.Image, containerConfig)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(runResponse.containerId, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
	
	def delayedRun(String... args) {
		Map onelayer = this.executionContext.onelayer
		onelayer.run << [args.join(" ")]
	}
}
