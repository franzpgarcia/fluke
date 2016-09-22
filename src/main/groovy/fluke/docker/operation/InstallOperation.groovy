package fluke.docker.operation

import fluke.core.annotation.AllowedIn
import fluke.core.annotation.Keyword
import fluke.core.common.FlukeConsole
import fluke.core.exception.OperationException
import fluke.core.execution.ExecutionContext
import fluke.core.keyword.Keywords
import fluke.docker.api.DockerApi
import fluke.docker.common.HelperFunctions
import fluke.docker.packagemanager.OsPackageManager
import fluke.docker.packagemanager.PackageManager
import fluke.docker.shell.Bash

@AllowedIn([Keywords.IMAGE, Keywords.PROCEDURE, Keywords.WITH])
@Keyword(Keywords.INSTALL)
class InstallOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()

	InstallOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(List<String> pckages) {
		this.call(*pckges)
	}

	def call(String... pckages) {
		String pm = this.executionContext.currentPackageManager?:findDefaultPackageManager()
		this.call(PackageManager.get(this.executionContext, pm), pckages)
	}
	
	def call(PackageManager pm, List<String> pckages) {
		pm(pckages)
	}
	
	def call(PackageManager pm, String... pckages) {
		pm(pckages)
	}
	
	private String findDefaultPackageManager() {
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: buildFindPackageManagerCmd()]
			
		def runResponse = dockerApi.run(containerConfig.Image, containerConfig)
		dockerApi.remove(runResponse.containerId)
		String pm = runResponse.logs[0]
		if(pm == "none") {
			throw new OperationException("Unable to find the default package manager")
		}
		return pm
	}

	private List<String> buildFindPackageManagerCmd() {
		List<String> cmds = PackageManager.getAll(this.executionContext)
										  .entrySet()
										  .findAll {e -> e.value in OsPackageManager}
										  .collect { e -> "(" +e.value.getVersionCmd() + " &>/dev/null && echo \"" + e.key + "\")"}
		cmds << "echo 'none'"
		return new Bash(this.executionContext).buildShellCmd(cmds.join(" || "))
	}
	
}
