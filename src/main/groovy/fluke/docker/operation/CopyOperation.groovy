package fluke.docker.operation

import java.nio.file.Paths

import fluke.core.annotation.AllowedIn
import fluke.core.annotation.Keyword
import fluke.core.common.FlukeConsole
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.Keywords
import fluke.docker.api.DockerApi
import fluke.docker.common.HelperFunctions
import fluke.docker.common.TarCompressor

@AllowedIn([Keywords.IMAGE, Keywords.PROCEDURE, Keywords.WITH])
@Keyword(Keywords.COPY)
class CopyOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	private TarCompressor tarCompressor = new TarCompressor()

	CopyOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call(String src) {
		return new CopyTo(executionContext: executionContext, src: src)
	}
	
	def call(InputStream stream) {
		return new CopyTo(executionContext: executionContext, stream: stream)
	}
	
	def call(String source, String dest) {
		Map imageContext = this.executionContext.imageContext
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand(this.executionContext, "COPYING ${source} TO ${dest}")]
		
		console.printMessage "Copying ${source} to ${dest}"
		def containerResponse = dockerApi.createContainer(containerConfig)
		dockerApi.putArchive(containerResponse.id, dest, tarCompressor.tar(source))
		Map commitQuery =  HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(containerResponse.id, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}

	def call(InputStream stream, String dest) {
		Map imageContext = this.executionContext.imageContext
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildNoOpCommand(this.executionContext, "COPYING stream TO ${dest}")]
		
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

class CopyTo {
	ExecutionContext executionContext
	String src
	InputStream stream
	
	def to(String destination) {
		CopyOperation copyOp = new CopyOperation(this.executionContext)
		if(src) {
			copyOp(this.src, destination)
		} else if(stream) {
			copyOp(this.stream, destination)
		}
	}
}
