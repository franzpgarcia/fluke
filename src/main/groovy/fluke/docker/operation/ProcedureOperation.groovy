package fluke.docker.operation;

import groovy.lang.Closure;

import java.io.InputStream;
import java.util.Map;

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.block.BlockDefinition;
import fluke.core.common.FlukeConsole;
import fluke.core.execution.ExecutionContext;
import fluke.core.keyword.Keywords;
import fluke.docker.block.ProcedureBlock;

@AllowedIn([Keywords.FLUKE, Keywords.IMAGE])
@Keyword(Keywords.PROCEDURE)
class ProcedureOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	ProcedureOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(BlockDefinition definition) {
		this.call(definition.name, definition.closure)
	}
	
	def call(String procedure, Closure closure) {
		this.executionContext.procedures[procedure] = new ProcedureBlock(closure: closure)
	}
	
	def call(Map<String, Closure> procedures) {
		procedures.each {
			k, v -> this.call(k, v)
		}
	}
}
