package fluke.operation

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.api.DockerApi
import fluke.common.FlukeConsole;
import fluke.common.HelperFunctions;
import fluke.exception.InvalidCallException;
import fluke.exception.NotImplementedYetException;
import fluke.exception.OperationException;
import fluke.execution.ExecutionContext;
import fluke.packagemanager.OsPackageManager;
import fluke.packagemanager.PackageManager;

@AllowedIn(["image", "procedure", "with"])
@Keyword("install")
class InstallOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()

	InstallOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call(String pckage) {
		Map variables = this.executionContext.variables
		String pm = variables.currentPackageManager?:findDefaultPackageManager()
		this.call(pm, pckage)
	}
	
	def call(String pm, String pckage) {
		this.call(PackageManager.get(this.executionContext, pm), pckage)
	}
	
	def call(PackageManager pm, String pckage) {
		pm(pckage)
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
		return HelperFunctions.buildShellCommand(this.executionContext, cmds.join(" || "))
	}
	
}
