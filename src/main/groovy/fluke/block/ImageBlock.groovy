package fluke.block

import groovy.lang.Closure;

import java.util.Map;

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.AllowedOperations;
import fluke.api.DockerApi;
import fluke.block.ProcedureBlock;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@AllowedOperations(["procedure", "from", "apply", "volume", "port", "onstart", "port", "setenv"])
class ImageBlock implements ExecutableBlock {
	String image
	String maintainer
	Map<String, String> labels

	private DockerApi dockerApi = new DockerApi()
	
	@Override
	def beforeExecute() {
		this.executionContext.variables["imageContext"] = [image: image + new Random().nextInt()]
		this.executionContext.variables["currentUser"] = "root"
		this.executionContext.variables["currentDirectory"] = "/"
	}

	@Override
	def afterExecute(Map blockVars) {
		Map imageContext = this.executionContext.variables["imageContext"]?:[:]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)		
		def containerResponse = dockerApi.createContainer(containerConfig)
		
		println "Committing final image"
		imageContext["maintainer"] = blockVars.maintainer
		imageContext["labels"] = blockVars.labels
		def commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		def commitConfig =[Entrypoint: imageContext.onstart?.entrypoint,
						   Cmd: imageContext.onstart?.parameters,
						   WorkingDir: imageContext.onstart?.directory,
						   User: imageContext.onstart?.user]
		
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, commitConfig, true).imageId
		println "=> ${imageContext.currentImageId}"
	}
	
}