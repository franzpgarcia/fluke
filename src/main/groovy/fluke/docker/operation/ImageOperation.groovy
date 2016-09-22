package fluke.docker.operation

import groovy.lang.Closure;

import java.util.Map;

import fluke.core.annotation.AllowedIn;
import fluke.core.annotation.Keyword;
import fluke.core.block.BlockDefinition;
import fluke.core.block.ExecutableBlock;
import fluke.core.common.FlukeConsole;
import fluke.core.execution.ExecutionContext;
import fluke.core.keyword.Keywords;
import fluke.docker.block.ImageBlock;
import fluke.docker.operation.FromOperation;

@AllowedIn(Keywords.FLUKE)
@Keyword(Keywords.IMAGE)
class ImageOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	ImageOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(BlockDefinition definition) {
		this.call(definition.name, definition.closure)
	}
	
	def call(String image, Closure closure) {
		this.executionContext.images[image] = new ImageBlock(image: image, closure: closure)
	}
	
	def call(Map<String, Closure> images) {
		images.each {
			k, v -> this.call(k, v)
		}
	}
	
}

