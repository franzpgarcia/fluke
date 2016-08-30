package fluke.operation;

import groovy.lang.Closure;

import java.io.InputStream;
import java.util.Map;

import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.block.ProcedureBlock;
import fluke.common.ConsoleOutputGenerator;
import fluke.execution.ExecutionContext;

@Operation("procedure")
class ProcedureOperation implements ConsoleOutputGenerator {
	private ExecutionContext executionContext
	
	ProcedureOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@OperationMethod
	def procedure(String procedure, Closure closure) {
		this.executionContext.procedures[procedure] = new ProcedureBlock(block: closure)
	}
	
	@OperationMethod
	def procedure(Map<String, Closure> procedures) {
		procedures.each {
			k, v -> this.procedure(k, v)
		}
	}
}
