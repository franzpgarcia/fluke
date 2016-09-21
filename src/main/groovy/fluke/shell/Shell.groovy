package fluke.shell

import java.util.Map;

import org.reflections.Reflections;

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.exception.InvalidShellException
import fluke.execution.ExecutionContext
import fluke.operation.RunOperation;
import fluke.packagemanager.PackageManager;

@AllowedIn(["image", "procedure", "with"])
abstract class Shell {
	private static final Map<String, Class> shellClasses
	
	static {
		Reflections reflections = new Reflections("fluke.shell");
		shellClasses = reflections.getTypesAnnotatedWith(Keyword.class).collectEntries {
			[(it.getAnnotation(Keyword.class).value()): it]
		}.asImmutable()
	}
	ExecutionContext executionContext
		
	abstract List<String> buildShellCmd(String... args)
	
	def call(String... args) {
		RunOperation runOp = new RunOperation(executionContext)
		runOp(this.buildShellCmd(args))
	}
	
	def call(List<String> cmd) {
		this.call(*cmd)
	}
	
	public static Shell get(ExecutionContext context, String keyword) {
		Class shellClass = shellClasses[keyword]
		if(shellClass) {
			return shellClass.newInstance(context)
		} else {
			throw new InvalidShellException("Invalid shell ${keyword}")
		}
	}
	
	public static Map<String, Shell> getAll(ExecutionContext context) {
		return shellClasses.collect {[(it.key): it.value.newInstance(context)]}
	}
}
