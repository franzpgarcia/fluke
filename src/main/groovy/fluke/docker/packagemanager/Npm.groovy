package fluke.docker.packagemanager

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.execution.ExecutionContext;

@Keyword("npm")
class Npm extends PackageManager {
	
	Npm(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	public String getUpdateRepositoriesCmd() {
		return null;
	}

	@Override
	public String getVersionCmd() {
		return "npm --version"
	}

	@Override
	public String getInstallPackageCmd(String... pckageArgs) {
		return "npm install ${pckageArgs}"
	}

}
