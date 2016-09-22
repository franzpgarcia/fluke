package fluke.operation;

import groovy.lang.Closure;

import java.io.InputStream;
import java.util.Map;

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.block.ProcedureBlock;
import fluke.common.FlukeConsole;
import fluke.definition.Definition;
import fluke.execution.ExecutionContext;
import fluke.keyword.Keywords;

@AllowedIn([Keywords.FLUKE, Keywords.IMAGE])
@Keyword(Keywords.PROCEDURE)
class ProcedureOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	ProcedureOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(Definition definition) {
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
