package fluke.operation

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.block.WithBlock;
import fluke.common.FlukeConsole;
import fluke.exception.OperationException
import fluke.execution.ExecutionContext;
import fluke.keyword.Keywords;
import fluke.packagemanager.PackageManager;
import fluke.shell.Shell

@AllowedIn([Keywords.IMAGE, Keywords.PROCEDURE, Keywords.WITH])
@Keyword(Keywords.WITH)
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
		WithBlock withBlock = new WithBlock(closure: closure, user: useMap.user, directory: useMap.directory)
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
	
	def run(List<String> args) {
		this.run(*args)
	}
	
	def run(String... args) {
		Shell shell = Shell.get(executionContext, with)
		shell(args)
	}
	
	def install(List<String> pckages) {
		this.install(*pckages)
	}
	
	def install(String... pckages) {
		InstallOperation installOp = new InstallOperation(executionContext)
		installOp(PackageManager.get(this.executionContext, with), pckages)
	}
}

class WithShell {
	ExecutionContext executionContext
	Shell shell
	
	def run(String... args) {
		shell(args)
	}
	
	def run(List<String> args) {
		shell(args)
	}
}

class WithPm {
	ExecutionContext executionContext
	PackageManager pm
	
	def install(List<String> pckages) {
		this.install(*pckages)
	}
	
	def install(String... pckages) {
		InstallOperation installOp = new InstallOperation(executionContext)
		installOp(pm, pckages)
	}
}