package fluke.operation

import groovy.lang.Closure;

import java.util.Map;

import fluke.block.ExecutableBlock;
import fluke.block.ImageBlock;
import fluke.common.FlukeConsole;
import fluke.definition.Definition;
import fluke.execution.ExecutionContext;
import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.keyword.Keywords;
import fluke.operation.FromOperation;

@AllowedIn(Keywords.FLUKE)
@Keyword(Keywords.IMAGE)
class ImageOperation {
	private static FlukeConsole console = FlukeConsole.getConsole()
	
	private ExecutionContext executionContext
	
	ImageOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	def call(Definition definition) {
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

