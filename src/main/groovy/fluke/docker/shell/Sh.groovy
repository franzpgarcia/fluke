package fluke.docker.shell

import java.util.List;

import fluke.core.annotation.Keyword;
import fluke.core.execution.ExecutionContext;

@Keyword("sh")
class Sh extends Shell {
	
	Sh(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@Override
	List<String> buildShellCmd(String... args) {
		return ["/bin/sh", "-c", args.join(" ")]
	}

}
