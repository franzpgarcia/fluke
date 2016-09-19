package fluke.operation

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi
import fluke.common.FlukeConsole;
import fluke.common.HelperFunctions;
import fluke.exception.InvalidOperationCallException;
import fluke.exception.NotImplementedYetException;
import fluke.exception.OperationException;
import fluke.execution.ExecutionContext;

@Operation("install")
class InstallOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()

	InstallOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def install(Map<String, String> install) {
		String pckage = install["package"]		
		installPackage(pckage)

	}

	private void installPackage(String pckage) {
		Map imageContext = this.executionContext.variables.imageContext
		boolean update = imageContext.packageManager == null
		String packageManager = imageContext.packageManager?:findPackageManager()
		
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: buildInstallPackageCmd(packageManager, pckage, update)]

		console.printMessage "Installing package ${pckage}"
		def runResponse = dockerApi.run(containerConfig.Image, containerConfig, true)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(runResponse.containerId, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
	
	private String findPackageManager() {
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: buildFindPackageManagerCmd()]
			
		def runResponse = dockerApi.run(containerConfig.Image, containerConfig)
		dockerApi.remove(runResponse.containerId)
		return runResponse.logs[0]
	}

	private List<String> buildFindPackageManagerCmd() {
		String cmd = ""
		for(String pm in ["apt-get", "yum", "zypper"]) {
			cmd += "(${pm} --version &>/dev/null && echo \"${pm}\") ||"
		}
		cmd += "echo 'none'"
		return HelperFunctions.buildShellCommand(this.executionContext, cmd)
	}

	private List<String> buildInstallPackageCmd(String packageManager, String pckge, boolean update = false) {
		String cmd = "";
		switch(packageManager) {
			case "apt-get": 
				cmd += update ? "apt-get update && " : ""
				cmd += "apt-get -y install ${pckge}"
				break
			case "yum": 
				cmd += update ? "yum update && " : ""
				cmd += "yum -y install ${pckge}"
				break
			case "zypper":
				cmd += update ? "zypper update && " : ""
				cmd += "zypper --non-interactive install ${pckge}"
				break
			case "none": 
				throw new OperationException("Unable to install package. Package manager couldn't be found.")
		}
		return HelperFunctions.buildShellCommand(this.executionContext, cmd)
	}
	
}
