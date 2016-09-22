package fluke.docker.operation

import fluke.core.annotation.AllowedIn
import fluke.core.annotation.Keyword
import fluke.core.common.FlukeConsole
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.Keywords
import fluke.docker.api.DockerApi
import fluke.docker.common.HelperFunctions

@AllowedIn([Keywords.IMAGE, Keywords.PROCEDURE, Keywords.WITH])
@Keyword(Keywords.PORT)
class PortOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()

	PortOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call(int port) {
		this.call(port, "tcp")
	}

	def call(int port, String protocol) {
		Map imageContext = this.executionContext.imageContext
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand("EXPOSING ${port} ${protocol}"),
							ExposedPorts: ["${port}/${protocol}": [:]]]
		
		console.printMessage "Exposing port ${port}/${protocol}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
}
