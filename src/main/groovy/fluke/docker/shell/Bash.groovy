package fluke.docker.shell

import fluke.core.annotation.Keyword
import fluke.core.execution.ExecutionContext

@Keyword("bash")
class Bash extends Shell {

	Bash(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	List<String> buildShellCmd(String... args) {
		return ["/bin/bash", "-c", args.join(" ")]
	}
}
