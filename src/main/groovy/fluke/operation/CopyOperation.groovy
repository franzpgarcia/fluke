package fluke.operation

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi;
import fluke.common.HelperFunctions;
import fluke.common.TarCompressor;
import fluke.execution.ExecutionContext;

@Operation("copy")
class CopyOperation {
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
		
		println "Copying ${source} to ${dest}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		dockerApi.putArchive(containerResponse.id, dest, tarCompressor.tar(source))
		Map commitQuery =  HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		println "=> ${imageContext.currentImageId}"
	}

	@OperationMethod
	def copy(InputStream stream, String name, String dest) {		
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand("COPYING stream TO ${dest}")]
		
		println "Copying file ${name} to ${dest}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		dockerApi.putArchive(containerResponse.id, dest, tarCompressor.tar(stream, name))
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		println "=> ${imageContext.currentImageId}"
	}
}
