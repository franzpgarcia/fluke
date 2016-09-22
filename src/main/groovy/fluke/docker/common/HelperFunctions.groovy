package fluke.docker.common

import fluke.core.exception.OperationException;
import fluke.core.execution.ExecutionContext;
import fluke.docker.shell.DefaultShell;

final class HelperFunctions {
	
	public static List<String> buildNoOpCommand(ExecutionContext executionContext, String cmd) {
		return new DefaultShell(executionContext).buildShellCmd("#(noop) ${cmd}")
	}

	public static Map buildContainerConfig(ExecutionContext executionContext) {
		Map imageContext = executionContext.imageContext
		
		String currentImageId = imageContext.currentImageId
		String user = executionContext.currentUser
		String directory = executionContext.currentDirectory
		if(!currentImageId) {
			throw new OperationException("Unable to proceed with build because of missing `from image`")
		}
		return [Image: currentImageId,
				User: user,
				WorkingDir: directory,
				Cmd: [""]]
	}

	public static Map buildCommitQuery(ExecutionContext executionContext) {
		Map imageContext = executionContext.imageContext
		
		String image = imageContext.image
		String tag = imageContext.tag
		String author = imageContext.maintainer
		return [repo: image, tag: tag, author: author]
	}
}
