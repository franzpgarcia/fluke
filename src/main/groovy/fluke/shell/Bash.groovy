package fluke.shell

import java.util.List;

import fluke.annotation.Keyword;
import fluke.execution.ExecutionContext;

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
