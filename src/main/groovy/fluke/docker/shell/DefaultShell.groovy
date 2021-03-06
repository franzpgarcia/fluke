package fluke.docker.shell

import fluke.core.annotation.Keyword
import fluke.core.execution.ExecutionContext

@Keyword("shell")
class DefaultShell extends Shell {

	DefaultShell(ExecutionContext executionContext) {
		this.executionContext = executionContext	
	}
	
	@Override
	List<String> buildShellCmd(String... cmd) {
		Shell defaultShell = findDefaultShell()
		return defaultShell.buildShellCmd(cmd)
	}

	private Shell findDefaultShell() {
		return new Bash(this.executionContext)
	}
}
