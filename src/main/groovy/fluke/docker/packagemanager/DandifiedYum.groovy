package fluke.docker.packagemanager

import fluke.core.annotation.Keyword;
import fluke.core.execution.ExecutionContext;

//@Keyword("dnf")
class DandifiedYum extends OsPackageManager {

	DandifiedYum(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	public String getUpdateRepositoriesCmd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersionCmd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInstallPackageCmd(String... pckageArgs) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
