package fluke.operation

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.block.WithBlock;
import fluke.common.FlukeConsole;
import fluke.exception.OperationException
import fluke.execution.ExecutionContext;
import fluke.packagemanager.PackageManager;
import fluke.shell.Shell

@AllowedIn(["image", "procedure", "with"])
@Keyword("with")
class WithOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	WithOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(Map<String, String> useMap) {
		withUser(useMap.user)
		withDirectory(useMap.directory)
		withShell(useMap.shell)
		withPackageManager(useMap.pm)
	}
	
	def call(Map<String, String> useMap, Closure closure) {
		WithBlock withBlock = new WithBlock(block: closure, user: useMap.user, directory: useMap.directory)
		withBlock(this.executionContext)
	}
	
	def call(String with) {
		return new DelayedWith(executionContext: this.executionContext, with: with)
	}
	
	def call(Shell shell) {
		return new WithShell(executionContext: this.executionContext, shell: shell)
	}
	
	def call(PackageManager pm) {
		return new WithPm(executionContext: this.executionContext, pm: pm)
	}
	
	private void withUser(String user) {
		if(user) {
			this.executionContext.variables["currentUser"] = user
		} 
	}
	
	private void withDirectory(String directory) {
		if(directory) {
			this.executionContext.variables["currentDirectory"] = directory
		}
	}
	
	private void withShell(String shell) {
		if(shell) {
			this.executionContext.variables["currentShell"] = shell
		}
	}
	
	private void withPackageManager(String pm) {
		if(pm) {
			this.executionContext.variables["currentPackageManager"] = pm
		}
	}
}

class DelayedWith {
	ExecutionContext executionContext
	String with
	
	def run(String... args) {
		Shell shell = Shell.get(executionContext, with)
		shell(args)
	}
	
	def install(String name) {
		InstallOperation installOp = new InstallOperation(executionContext)
		installOp(with, name)
	}
}

class WithShell {
	ExecutionContext executionContext
	Shell shell
	
	def run(String... args) {
		shell(args)
	}
}

class WithPm {
	ExecutionContext executionContext
	PackageManager pm
	
	def install(String name) {
		InstallOperation installOp = new InstallOperation(executionContext)
		installOp(pm, name)
	}
}