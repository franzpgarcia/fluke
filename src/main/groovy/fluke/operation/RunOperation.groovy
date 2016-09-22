package fluke.operation

import java.util.stream.Collectors;

import javax.xml.ws.soap.AddressingFeature.Responses;

import org.apache.commons.compress.utils.IOUtils;

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.api.DockerApi;
import fluke.block.ExecutableBlock;
import fluke.common.FlukeConsole;
import fluke.common.HelperFunctions;
import fluke.execution.ExecutionContext;
import fluke.keyword.Keywords;

@AllowedIn([Keywords.IMAGE, Keywords.PROCEDURE, Keywords.WITH])
@Keyword(Keywords.RUN)
class RunOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	RunOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(Closure closure, List<String> args) {
		List<String> adaptedArgs = closure(this.executionContext, args)
		ExecutableBlock currentBlock = this.executionContext.currentBlock
		runNow(adaptedArgs)
	}
	
	def call(Closure closure, String... args) {
		this.call(closure, args as List)
	}
	
	def call(String... args) {
		this.call(getShellClosure(), args)
	}
	
	def call(List<String> args) {
		this.call(getShellClosure(), args)
	}
	
	private Closure getShellClosure() {
		if(this.executionContext.variables.currentShell) {
			return HelperFunctions.&buildShellCommand
		}
		return {ctx, i -> i}
	}
	
	def runNow(List<String> args) {
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: args]
		
		console.printMessage "Running command ${args}"
		def runResponse = dockerApi.run(containerConfig.Image, containerConfig)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(runResponse.containerId, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
	
	def delayedRun(List<String> args) {
		Map onelayer = this.executionContext.variables.onelayer
		onelayer.run << [args.join(" ")]
	}
}
