package fluke.shell

import java.util.List;

import fluke.annotation.Keyword;
import fluke.exception.NotImplementedYetException;
import fluke.execution.ExecutionContext

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
