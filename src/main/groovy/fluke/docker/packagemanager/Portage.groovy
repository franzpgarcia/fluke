package fluke.docker.packagemanager

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.execution.ExecutionContext;

//@Keyword("emerge")
class Portage extends OsPackageManager {
	
	private static final String EMERGE = "emerge"

	Portage(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	public String getUpdateRepositoriesCmd() {
		return null;
	}

	@Override
	public String getVersionCmd() {
		return null;
	}

	@Override
	public String getInstallPackageCmd(String... pckageArgs) {
		return null;
	}

}
