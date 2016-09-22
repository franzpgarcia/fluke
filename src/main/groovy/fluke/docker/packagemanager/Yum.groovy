package fluke.docker.packagemanager

import fluke.core.annotation.Keyword
import fluke.core.execution.ExecutionContext

@Keyword("yum")
class Yum extends OsPackageManager {
	
	private static final String YUM = "yum"
	
	Yum(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	String getUpdateRepositoriesCmd() {
		return "${YUM} update"
	}
	
	@Override
	String getVersionCmd() {
		return "${YUM} --version"
	}
	
	@Override
	String getInstallPackageCmd(String... pckageArgs) {
		return "${YUM} -y install ${pckgeArgs}"
	}

}
