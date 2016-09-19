package fluke.common

import fluke.exception.OperationException
import fluke.execution.ExecutionContext;

final class HelperFunctions {
	public static final String DEFAULT_SHELL = "sh"
	
	public static List<String> buildShellCommand(ExecutionContext executionContext, List<String> args) {
		buildShellCommand(executionContext, args.join(" "))
	}
	
	public static List<String> buildShellCommand(ExecutionContext executionContext, String cmd) {
		String name = executionContext.variables.currentShell?:HelperFunctions.DEFAULT_SHELL
		switch(name) {
			case "bash": return ["/bin/bash", "-c", cmd]
			case "sh": return ["/bin/sh", "-c", cmd]
		}
		return [name, cmd]
	}

	public static List<String> buildNoOpCommand(String cmd) {
		return buildShellCommand(DEFAULT_SHELL, "#(noop) ${cmd}")
	}

	public static Map buildContainerConfig(ExecutionContext executionContext) {
		Map variables = executionContext.variables
		Map imageContext = variables.imageContext
		
		String currentImageId = imageContext.currentImageId
		String user = variables.currentUser
		String directory = variables.currentDirectory
		if(!currentImageId) {
			throw new OperationException("Unable to proceed with build because of missing `from image`")
		}
		return [Image: currentImageId,
				User: user,
				WorkingDir: directory,
				Cmd: [""]]
	}

	public static Map buildCommitQuery(ExecutionContext executionContext) {
		Map variables = executionContext.variables
		Map imageContext = variables.imageContext
		
		String image = imageContext.image
		String tag = imageContext.tag
		String author = imageContext.maintainer
		return [repo: image, tag: tag, author: author]
	}
}
