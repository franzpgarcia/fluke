package fluke

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.reflections.Reflections;
import org.apache.commons.cli.Option

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;
import de.gesellix.docker.client.DockerResponse;
import fluke.annotation.AllowedOperations;
import fluke.annotation.Operation;
import fluke.cli.FlukeCli;
import fluke.common.BuiltInFunctions;
import fluke.common.TarCompressor;
import fluke.execution.BlockExecution;
import fluke.execution.ExecutionContext;
import fluke.operation.map.OperationMap;
import fluke.script.ScriptExecution;
import fluke.script.ScriptRunner;

class App implements BuiltInFunctions {

	static void main(String[] args) {
		FlukeCli flukeCli = new FlukeCli()
		flukeCli.handle(args)
	}
}