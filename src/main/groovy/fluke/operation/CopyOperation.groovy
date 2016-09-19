package fluke.operation

import java.nio.file.Path;
import java.nio.file.Paths;

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.common.FlukeConsole;
import fluke.common.HelperFunctions;
import fluke.common.TarCompressor;
import fluke.execution.ExecutionContext;

@Operation("copy")
class CopyOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	private TarCompressor tarCompressor = new TarCompressor()

	CopyOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def copy(String source, String dest) {
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand("COPYING ${source} TO ${dest}")]
		
		console.printMessage "Copying ${source} to ${dest}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		dockerApi.putArchive(containerResponse.id, dest, tarCompressor.tar(source))
		Map commitQuery =  HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}

	@OperationMethod
	def copy(InputStream stream, String dest) {		
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand("COPYING stream TO ${dest}")]
		
		console.printMessage "Copying file stream to ${dest}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		dockerApi.putArchive(containerResponse.id, getParent(dest), tarCompressor.tar(stream, getFilename(dest)))
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
	
	private String getFilename(String dest) {
		String path = Paths.get(dest).fileName.toString()
	}
	
	private String getParent(String dest) {
		String path = Paths.get(dest).parent
		return dest.contains("\\") ? path : path.replace("\\", "/") 
	}
}
