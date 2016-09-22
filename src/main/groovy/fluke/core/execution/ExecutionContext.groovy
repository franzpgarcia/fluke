package fluke.core.execution

import fluke.core.block.ExecutableBlock;

class ExecutionContext extends LinkedHashMap<String, Object> {
	private ExecutableBlock currentBlock
	
	ExecutionContext() {
		this(null)
	}
	
	ExecutionContext(ExecutableBlock currentBlock) {
		this(currentBlock, [:])
	}
	
	ExecutionContext(ExecutableBlock currentBlock, Map values) {
		this.currentBlock = currentBlock
		this << values
	}
	
	ExecutionContext copy() {
		return this.copy(this)
	}
	
	ExecutionContext copy(ExecutableBlock currentBlock) {
		ExecutionContext context = this.clone()
		context.currentBlock = this.currentBlock
		return context
	}
	
	public ExecutableBlock getCurrentBlock() {
		return this.currentBlock
	}
}

