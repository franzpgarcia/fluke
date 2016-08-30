package fluke.exception
class InvalidOperationCallException extends RuntimeException {
	InvalidOperationCallException(String operation, String block) {		super(block?"Cannot execute `${operation}` operation in a `${block}` block":"Cannot execute `${operation}` at that location")	}	
}
