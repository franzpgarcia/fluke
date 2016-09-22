package fluke.docker.packagemanager

import fluke.core.annotation.Keyword
import fluke.core.execution.ExecutionContext

@Keyword("apt-get")
class AptGet extends OsPackageManager {	
	private static final String APT_GET = "apt-get"
	
	AptGet(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	String getUpdateRepositoriesCmd() {
		return "${APT_GET} update"
	}
	
	@Override
	String getVersionCmd() {
		return "${APT_GET} --version"
	}
	
	@Override
	String getInstallPackageCmd(String... pckageArgs) {
		return "${APT_GET} -y install ${pckageArgs}"
	}

}
