package fluke.packagemanager

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.execution.ExecutionContext;

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
