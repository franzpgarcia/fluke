package fluke.packagemanager

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.execution.ExecutionContext;

@Keyword("pacman")
class Pacman extends OsPackageManager {
	
	private static final String PACMAN = "pacman"
	
	Pacman(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	String getUpdateRepositoriesCmd() {
		return "${PACMAN} -Syyu -—noconfirm"
	}
	
	@Override
	String getVersionCmd() {
		return "${PACMAN} --version"
	}
	
	@Override
	String getInstallPackageCmd(String... pckageArgs) {
		return "${PACMAN} -S --noconfirm ${pckageArgs}"
	}

}
