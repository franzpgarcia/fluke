package fluke.common

import fluke.exception.OperationException
import fluke.execution.ExecutionContext;
import fluke.shell.DefaultShell;

final class HelperFunctions {
	
	public static List<String> buildNoOpCommand(ExecutionContext executionContext, String cmd) {
		return new DefaultShell(executionContext).buildShellCmd("#(noop) ${cmd}")
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
