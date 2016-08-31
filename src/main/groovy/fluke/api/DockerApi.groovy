package fluke.api

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientException;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.exception.DockerApiException;
import fluke.exception.OperationException;

class DockerApi {
	private DockerClient dockerClient = new DockerClientImpl()

	Map run(String image, Map containerConfig) {
		try {
			Map result = [:]
			def runResponse = dockerClient.run(image, containerConfig)
			result << [containerId: runResponse["container"]["content"]["Id"]]
			result << this.wait(result.containerId)
			result << this.logs(result.containerId)
			return result
		} catch(DockerApiException e) {
			throw new DockerApiException("Unable to run image ${image} with command ${containerConfig.Cmd}")
		}
	}

	Map wait(String containerId) {
		try {
			def waitResponse = dockerClient.wait(containerId)
			return [statusCode: waitResponse["content"]["StatusCode"]]
		} catch(DockerApiException e) {
			throw new DockerApiException("Issue while waiting for container ${containerId}")
		}
	}

	Map logs(String containerId) {
		try {
			def logResponse =  dockerClient.logs(containerId, [:])
			List<String> logs = new BufferedReader(new InputStreamReader(logResponse["stream"], "UTF-8")).lines().collect { String log ->
				log.replaceAll("[^\\x20-\\x7e]", "")
			}
			return [logs: logs]
		} catch(DockerApiException) {
			throw new DockerApiException("Unable to get logs for ${containerId}")
		}
	}

	void remove(String containerId) {
		dockerClient.rm(containerId)
	}

	Map commit(String containerId, Map commitQuery, boolean deleteAfterCommit = false) {
		try {
			def commitResponse = dockerClient.commit(containerId, commitQuery)
			return [imageId: commitResponse["content"]["Id"]]
		} catch(DockerApiException e) {
			throw new DockerApiException("Unable to commit ${containerId}")
		} finally {
			if(deleteAfterCommit) {
				this.remove(containerId)
			}
		}
	}

	Map commit(String containerId, Map commitQuery, Map commitConfig, boolean deleteAfterCommit = false) {
		try {
			def commitResponse = dockerClient.commit(containerId, commitQuery, commitConfig)
			return [imageId: commitResponse["content"]["Id"]]
		} catch(DockerApiException e) {
			throw new DockerApiException("Unable to commit ${containerId}")
		} finally {
			if(deleteAfterCommit) {
				this.remove(containerId)
			}
		}
	}

	Map createContainer(Map containerConfig) {
		try {
			def containerResponse = dockerClient.createContainer(containerConfig)
			return [id: containerResponse["content"]["Id"]]
		} catch(DockerApiException e) {
			throw new DockerApiException("Unable to create container")
		}
	}

	void putArchive(String containerId, String dest, InputStream archiveStream) {
		try {
			dockerClient.putArchive(containerId, dest, archiveStream)
		} catch(DockerApiException e) {
			throw new DockerApiException("Unable to push file(s) to container ${containerId} in directory ${dest}")
		}
	}

	Map pull(String image, String tag) {
		try {
			def pullResponse = [id: dockerClient.pull(image, tag?:"latest")]
		} catch(DockerClientException e) {
			throw new DockerApiException("Unable to pull image ${image}:${tag}")
		}
	}

	void rmi(String imageId) {
		try{
			dockerClient.rmi(imageId)
		} catch(DockerClientException e) {
			throw new DockerApiException("Unable to remove image ${imageId}")
		}
	}
	
	Boolean imageExists(String image) {
		try{
			def response = dockerClient.inspectImage(image)
			println response.content != null
			return response.content?.Id != null
		} catch(DockerClientException e) {
			return false
		}
	}
}
