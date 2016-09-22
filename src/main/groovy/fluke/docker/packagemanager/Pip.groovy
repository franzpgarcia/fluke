package fluke.docker.packagemanager

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.execution.ExecutionContext;

@Keyword("pip")
class Pip extends PackageManager {

	Pip(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	public String getUpdateRepositoriesCmd() {
		return null;
	}

	@Override
	public String getVersionCmd() {
		return "pip --version";
	}

	@Override
	public String getInstallPackageCmd(String... pckageArgs) {
		return "pip install ${pckageArgs}";
	}
	
}
