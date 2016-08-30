package fluke.operation;

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.block.ProcedureBlock;
import fluke.block.ImageBlock;
import fluke.common.ConsoleOutputGenerator;
import fluke.exception.NotImplementedYetException;
import fluke.execution.ExecutionContext;

@Operation("apply")
class ApplyOperation implements ConsoleOutputGenerator {
	
	private ExecutionContext executionContext
	
	ApplyOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}

	@OperationMethod
	def apply(Map<String, Object> apply) {
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
				procedureBlock.eval(this.executionContext)
			} else {
				//TODO throw exception
			}
		}
	}
	
	private void applyScript(Map<String, Object> apply) {
		def script = apply["script"]
		if(script) {
			throw new NotImplementedYetException()
			/*if(script in String) {
				
			} else if(script in InputStream) {
			
			}*/
		}
	}
	
}
