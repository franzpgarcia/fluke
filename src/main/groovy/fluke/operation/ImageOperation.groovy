package fluke.operation

import groovy.lang.Closure;

import java.util.Map;

import fluke.block.ExecutableBlock;
import fluke.block.ImageBlock;
import fluke.execution.ExecutionContext;
import fluke.annotation.Operation;
import fluke.annotation.OperationMethod;
import fluke.operation.FromOperation;

@Operation("image")
class ImageOperation {
	private ExecutionContext executionContext
	
	ImageOperation(ExecutionContext executionContext) {
		this.executionContext = executionContext
	}
	
	@OperationMethod
	def image(String image, Closure closure) {
		this.executionContext.images[image] = new ImageBlock(image: image, block: closure)
	}
	
	@OperationMethod
	def image(Map<String, Closure> images) {
		images.each {
			k, v -> this.image(k, v)
		}
	}
	
}

