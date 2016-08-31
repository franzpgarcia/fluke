package fluke.block

import groovy.lang.Closure;

import java.util.Map;

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.AllowedOperations;
import fluke.annotation.Block;
import fluke.api.DockerApi;
import fluke.block.ProcedureBlock;
import fluke.common.ConsoleOutputGenerator;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@Block(of="image")
@AllowedOperations(["procedure", "from", "apply", "volume", "port", "onstart", "port", "setenv"])
class ImageBlock implements ExecutableBlock, ConsoleOutputGenerator {
	String image
	String maintainer
	Map<String, String> labels

	private DockerApi dockerApi = new DockerApi()
	
	@Override
	def beforeExecute(ExecutionContext executionContext) {
		executionContext.variables["currentUser"] = "root"
		executionContext.variables["currentDirectory"] = "/"
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
		
		printMessage "Committing final image. Maintainer=${blockVars.maintainer}, Labels=${blockVars.labels}"
		imageContext["maintainer"] = blockVars.maintainer
		def commitQuery = HelperFunctions.buildCommitQuery(executionContext)
		def commitConfig =[Entrypoint: imageContext.onstart?.entrypoint,
						   Cmd: imageContext.onstart?.parameters,
						   WorkingDir: imageContext.onstart?.directory,
						   User: imageContext.onstart?.user,
						   Labels: blockVars.labels]
		
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, commitConfig, true).imageId
		printCommit imageContext.currentImageId
	}
	
}