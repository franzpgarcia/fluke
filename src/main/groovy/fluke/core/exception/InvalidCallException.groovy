package fluke.core.exception
class InvalidCallException extends RuntimeException {
	InvalidCallException(String keyword, String block) {		super(block?"Cannot execute `${keyword}` operation in a `${block}` block":"Cannot execute `${keyword}` at that location")	}	
}
