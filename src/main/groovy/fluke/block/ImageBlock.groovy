package fluke.block

import groovy.lang.Closure;

import java.util.Map;

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.AllowedOperations;
import fluke.annotation.Block;
import fluke.api.DockerApi;
import fluke.block.ProcedureBlock;
import fluke.common.FlukeConsole;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@Block(of="image")
@AllowedOperations(["procedure", "from", "apply", "onstart", "run", "port", "install", "copy", "volume", "with", "setenv"])
class ImageBlock implements ExecutableBlock {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	String image
	String maintainer
	Map<String, String> labels

	private DockerApi dockerApi = new DockerApi()
	
	@Override
	def beforeExecute(ExecutionContext executionContext) {
		executionContext.variables["currentUser"] = "root"
		executionContext.variables["currentDirectory"] = "/"
		executionContext.variables["currentShell"] = null
	}
	
	@Override
	def onError(Exception error, ExecutionContext executionContext) {
		Map imageContext = executionContext.variables.imageContext
		if(imageContext.currentImageId) {
			dockerApi.rmi(imageContext.currentImageId)
		}
		throw error
	}

	@Override
	def afterExecute(ExecutionContext executionContext, Map blockVars) {
		Map imageContext = executionContext.variables["imageContext"]?:[:]
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