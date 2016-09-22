package fluke.docker.packagemanager

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.execution.ExecutionContext;

@Keyword("zypper")
class Zypper extends OsPackageManager {	
	
	private static final String ZYPPER = "zypper"
	
	Zypper(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	String getUpdateRepositoriesCmd() {
		return "${ZYPPER} update"
	}
	
	@Override
	String getVersionCmd() {
		return "${ZYPPER} --version"
	}
	
	@Override
	String getInstallPackageCmd(String... pckageArgs) {
		return "zypper --non-interactive install ${pckageArgs}"
	}

}
