package fluke.docker.packagemanager

import java.util.List;

import org.reflections.Reflections;
import org.reflections.ReflectionsException;

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.common.FlukeConsole;
import fluke.core.execution.ExecutionContext;
import fluke.docker.api.DockerApi;
import fluke.docker.common.HelperFunctions;
import fluke.docker.exception.InvalidPackageManagerException;
import fluke.docker.shell.Bash;

@AllowedIn(["image", "procedure", "with"])
abstract class PackageManager {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private static final Map<String, Class> pmClasses
	
	static {
		Reflections reflections = new Reflections("fluke.docker.packagemanager");
		pmClasses = reflections.getTypesAnnotatedWith(Keyword.class).collectEntries {
			[(it.getAnnotation(Keyword.class).value()): it]
		}.asImmutable()
	}
	
	ExecutionContext executionContext
	private DockerApi dockerApi = new DockerApi()
	
	def call(String... pckages) {
		Map imageContext = this.executionContext.imageContext
		boolean update = true
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: buildInstallPackageCmd(pckages.join(" "), update)]

		console.printMessage "Installing package ${pckages}"
		def runResponse = dockerApi.run(containerConfig.Image, containerConfig, true)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(runResponse.containerId, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
	
	abstract String getUpdateRepositoriesCmd()
	
	abstract String getVersionCmd()
	
	abstract String getInstallPackageCmd(String... pckageArgs)
	
	private List<String> buildInstallPackageCmd(String pckge, boolean update = false) {
		List<String> cmds = []
		String updateRepoCmd = this.getUpdateRepositoriesCmd()
		if(update && updateRepoCmd) {
			cmds << "${updateRepoCmd}"
		}
		cmds << this.getInstallPackageCmd(pckge)
		return new Bash(this.executionContext).buildShellCmd(cmds.join(" && "))
	}
	
	public static PackageManager get(ExecutionContext context, String keyword) {
		Class pmClass = pmClasses[keyword]
		if(pmClass) {
			return pmClass.newInstance(context)
		} else {
			throw new InvalidPackageManagerException("Invalid package manager ${keyword}")
		}
	}
	
	public static Map<String, PackageManager> getAll(ExecutionContext context) {
		return pmClasses.collectEntries {
			[(it.key): it.value.newInstance(context)]
		}
	}
}
