package fluke.docker.packagemanager

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.execution.ExecutionContext;

@Keyword("gem")
class RubyGem extends PackageManager {

	RubyGem(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	public String getUpdateRepositoriesCmd() {
		return null
	}

	@Override
	public String getVersionCmd() {
		return "gem --version"
	}

	@Override
	public String getInstallPackageCmd(String... pckageArgs) {
		return "gem install ${pckageArgs}"
	}

}
