package fluke.docker.block

import fluke.core.annotation.Block
import fluke.core.block.ExecutableBlock
import fluke.core.common.FlukeConsole
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.Keywords
import fluke.docker.api.DockerApi
import fluke.docker.common.HelperFunctions

@Block(of=Keywords.IMAGE)
class ImageBlock extends ExecutableBlock {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	String image
	String maintainer
	Map<String, String> labels

	private DockerApi dockerApi = new DockerApi()
	
	@Override
	def beforeExecute(ExecutionContext executionContext) {
		executionContext.currentUser = "root"
		executionContext.currentDirectory = "/"
		executionContext.currentShell = null
	}
	
	@Override
	def onError(Exception error, ExecutionContext executionContext) {
		Map imageContext = executionContext.imageContext
		if(imageContext.currentImageId && imageContext.fromImageId != imageContext.currentImageId) {
			dockerApi.rmi(imageContext.currentImageId)
		}
		throw error
	}

	@Override
	def afterExecute(ExecutionContext executionContext, Map blockVars) {
		Map imageContext = executionContext.imageContext?:[:]
		Map containerConfig = HelperFunctions.buildContainerConfig(executionContext)		
		def containerResponse = dockerApi.createContainer(containerConfig)
		
		console.printMessage "Committing final image. Maintainer=${blockVars.maintainer}, Labels=${blockVars.labels}"
		imageContext["maintainer"] = blockVars.maintainer
		def commitQuery = HelperFunctions.buildCommitQuery(executionContext)
		def commitConfig =[Entrypoint: imageContext.onstart?.entrypoint,
						   Cmd: imageContext.onstart?.parameters,
						   WorkingDir: imageContext.onstart?.directory,
						   User: imageContext.onstart?.user,
						   Labels: blockVars.labels]
		
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, commitConfig, true).imageId
		console.printCommit imageContext.currentImageId
	}
	
}