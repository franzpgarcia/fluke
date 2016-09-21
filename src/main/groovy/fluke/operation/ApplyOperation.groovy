package fluke.operation;

import java.nio.file.Paths;
import java.util.List;

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.api.DockerApi;
import fluke.block.ProcedureBlock;
import fluke.block.ImageBlock;
import fluke.common.BuiltInFunctions;
import fluke.common.FlukeConsole;
import fluke.common.HelperFunctions;
import fluke.exception.NotImplementedYetException;
import fluke.exception.OperationException;
import fluke.execution.ExecutionContext;

@AllowedIn("script")
@Keyword("apply")
class ApplyOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	private DockerApi dockerApi
	
	ApplyOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	def call(Map<String, Object> apply) {
		applyBuild(apply)
		applyScript(apply)
	}
	
	private void applyBuild(Map apply) {
		def procedure = apply["procedure"]
		if(procedure) {
			ProcedureBlock procedureBlock
			if(procedure in String) {
				procedureBlock = this.executionContext.procedures[procedure]
			} else if(procedure in Closure){
				procedureBlock = new ProcedureBlock(block: procedure)
			}
			if(procedureBlock) {
				procedureBlock(this.executionContext)
			} else {
				throw new OperationException("Procedure ${procedure} not defined")
			}
		}
	}
	
	private void applyScript(Map<String, Object> apply) {
		def script = apply["script"]
		if(script) {
			String scriptPath
			InputStream stream
			if(script in String) {
				console.printMessage "Running script ${script}"
				scriptPath = "~/${script}"
				stream = new FileInputStream(script)
			} else if(script in InputStream) {
				console.printMessage "Running script"
				scriptPath = "~/fluke_script_" + System.currentTimeMillis()
				stream = (InputStream) script
			} else {
				throw new OperationException("Script must be path or input stream.")
			}
			runScript(scriptPath, stream)
		}
	}
	
	private void runScript(String scriptPath, InputStream script) {
		String cmd = buildScriptCmd(scriptPath, script)
		Map imageContext = this.executionContext.variables["imageContext"]
		Map containerConfig = HelperFunctions.buildContainerConfig(this.executionContext)
		containerConfig << [Cmd: HelperFunctions.buildShellCommand(this.executionContext, cmd)]
		
		def runResponse = dockerApi.run(containerConfig.Image, containerConfig)
		Map commitQuery = HelperFunctions.buildCommitQuery(this.executionContext)
		imageContext.currentImageId = dockerApi.commit(runResponse.containerId, commitQuery, true).imageId
		console.printCommit imageContext.currentImageId
	}
	
	private String buildScriptCmd(String scriptPath, InputStream script) {
		String scriptStr = script.readLines().join("\n")
		String cmd = "echo \$'${scriptStr}' > ${scriptPath} && "
		cmd += "chmod 777 ${scriptPath} && "
		cmd += "${scriptPath} && "
		cmd += "rm -f ~/${scriptPath}"
		return cmd
	}
	
	private String getFilename(String dest) {
		String path = Paths.get(dest).fileName.toString()
	}
}
