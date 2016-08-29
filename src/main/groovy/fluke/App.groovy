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
		def scriptStr = '''
		def test() {
			println "test1"
		}
		
		procedure test: {
			
		}
		
		image hello: {
			maintainer = "Franz Garcia"
			onstart entrypoint: "/home/foo.sh", parameters: "-d"
			from image: "ubuntu"
			def f = {
				println "test"
			}
			def a = "foo"
			procedure test2: {
				run shell, "mkdir", "/blah1"
				with directory: "/home", {
					run shell, "mkdir", "bla"
				}
				run shell, "mkdir", "/blah2"
				port 22
				copy file("C:\\\\Users\\\\Franz\\\\Desktop\\\\movies.txt"), "my_movies", "/home/"
				//install package:"openssl"
			}
			apply procedure: "test2"
			volume "~/bla"
		}
		
		build image: "hello"
		'''
		FlukeCli flukeCli = new FlukeCli()
		flukeCli.handle(["-A", "foo=hello", "-A", "hello=world", "C:\\Users\\Franz\\Desktop\\test.fluke"] as String[])
		//flukeCli.handle(args)
		
	}
}