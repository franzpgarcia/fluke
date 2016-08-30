package fluke.exception;

class DockerApiException extends RuntimeException {

	DockerApiException() {
		super("Unable to proceed with build because of missing `from image`")
	}
}
