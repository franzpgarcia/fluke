package fluke.packagemanager

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.execution.ExecutionContext;

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
