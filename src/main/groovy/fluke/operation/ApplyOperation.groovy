package fluke.operation;

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.block.ProcedureBlock;
import fluke.block.ImageBlock;
import fluke.execution.ExecutionContext;

@Operation("apply")
class ApplyOperation {
	
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
		String procedure = apply["procedure"]
		if(procedure) {
			ProcedureBlock procedureBlock = this.executionContext.procedures[procedure]
			if(procedureBlock) {
				procedureBlock.eval()
			} else {
				//TODO throw exception
			}
		}
	}
	
	private void applyScript(Map<String, Object> apply) {
		def script = apply["script"]
		if(script) {
			if(script in String) {
				
			} else if(script in InputStream) {
			
			}
		}
	}
	
}
