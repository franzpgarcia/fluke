package fluke.operation

import groovy.lang.Closure;

import java.util.Map;

import fluke.block.ExecutableBlock;
import fluke.block.ImageBlock;
import fluke.common.FlukeConsole;
import fluke.execution.ExecutionContext;
import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.operation.FromOperation;

@AllowedIn("script")
@Keyword("image")
class ImageOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	ImageOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(String image, Closure closure) {
		this.executionContext.images[image] = new ImageBlock(image: image, block: closure)
	}
	
	def call(Map<String, Closure> images) {
		images.each {
			k, v -> this.call(k, v)
		}
	}
	
}

