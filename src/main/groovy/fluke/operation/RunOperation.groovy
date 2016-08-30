package fluke.operation

import java.util.stream.Collectors;

import javax.xml.ws.soap.AddressingFeature.Responses;

import org.apache.commons.compress.utils.IOUtils;

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.common.ConsoleOutputGenerator;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;

@Operation("run")
class RunOperation implements ConsoleOutputGenerator {
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	RunOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@OperationMethod
	def run(Closure closure, String... args) {
		this.run(closure(args.join(" ")))
	}
	
	@OperationMethod
	def run(String... args) {
		this.run(args as List)
	}
	
	@OperationMethod
	def run(List<String> args) {
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: args]
		
		printMessage "Running command ${args}"
		def runResponse = dockerApi.run(containerConfig.Image, containerConfig)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(runResponse.containerId, commitQuery, true).imageId
		printCommit imageContext.currentImageId
	}
	
}
