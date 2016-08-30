package fluke.exception;

class DockerApiException extends RuntimeException {

	DockerApiException(String message) {
		super(message)
	}
}
