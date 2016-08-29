package fluke.common

import fluke.execution.ExecutionContext;

final class HelperFunctions {

	public static List<String> buildShellCommand(String cmd) {
		List<String> cmds = ["/bin/sh", "-c", cmd]
		return cmds
	}

	public static List<String> buildNoOpCommand(String cmd) {
		return buildShellCommand("#(noop) ${cmd}")
	}

	public static Map buildContainerConfig(ExecutionContext executionContext) {
		Map imageContext = executionContext.variables["imageContext"]
		String currentImageId = imageContext["currentImageId"]
		String user = executionContext.variables["currentUser"]
		String directory = executionContext.variables["currentDirectory"]
		if(!currentImageId) {
			//TODO throw error
		}
		return [Image: currentImageId,
				User: user,
				WorkingDir: directory,
				Cmd: [""]]
	}

	public static Map buildCommitQuery(ExecutionContext executionContext) {
		Map imageContext = executionContext.variables["imageContext"]
		String image = imageContext["image"]
		String tag = imageContext["tag"]?:"latest"
		String author = imageContext["maintainer"]
		return [repo: image, tag: tag, author: author]
	}
}
