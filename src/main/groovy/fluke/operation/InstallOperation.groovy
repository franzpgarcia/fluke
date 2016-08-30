package fluke.operation

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.api.DockerApi
import fluke.common.ConsoleOutputGenerator;
import fluke.common.HelperFunctions;
import fluke.exception.InvalidOperationCallException;
import fluke.exception.NotImplementedYetException;
import fluke.execution.ExecutionContext;

@Operation("install")
class InstallOperation implements ConsoleOutputGenerator {
	
	private ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	InstallOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@OperationMethod
	def install(Map<String, String> install) {
		/*String pckage = install["package"]
		if(pckage) {
			println "Installing ${pckage}"
			//runOp.run(HelperFunctions.&buildShellCommand, "apt-get", "--assume-yes",  "install", pckage)
		}*/
		throw new NotImplementedYetException()
	}
	
	String getDistro() {
		RunOperation runOp = new RunOperation(executionContext)
		runOp.run(HelperFunctions.&buildShellCommand, "ls", "--format", "single-column", "--color=never", "/etdsadsac")
	}
	
	String getPackageManager(String distro) {
		switch(distro) {
			case "debian": return "apt-get"
		}
	}
	
}
