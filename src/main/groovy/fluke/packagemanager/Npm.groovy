package fluke.packagemanager

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.execution.ExecutionContext;

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
