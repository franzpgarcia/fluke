package fluke.api

import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientImpl;

class DockerApi {
	private DockerClient dockerClient = new DockerClientImpl()

	Map run(String image, Map containerConfig) {
		Map result = [:]
		def runResponse = dockerClient.run(image, containerConfig)
		result << [containerId: runResponse["container"]["content"]["Id"]]
		result << this.wait(result.containerId)
		result << this.logs(result.containerId)
		return result
	}

	Map wait(String containerId) {
		def waitResponse = dockerClient.wait(containerId)
		return [statusCode: waitResponse["content"]["StatusCode"]]
	}

	Map logs(String containerId) {
		def logResponse =  dockerClient.logs(containerId, [:])
		List<String> logs = new BufferedReader(new InputStreamReader(logResponse["stream"], "UTF-8")).lines().collect { String log ->
			log.replaceAll("[^\\x20-\\x7e]", "")
		}
		return [logs: logs]
	}
	
	void remove(String containerId) {
		dockerClient.rm(containerId)
	}
	
	Map commit(String containerId, Map commitQuery, boolean deleteAfterCommit = false) {
		try {
			def commitResponse = dockerClient.commit(containerId, commitQuery)
			return [imageId: commitResponse["content"]["Id"]]
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
		} finally {
			if(deleteAfterCommit) {
				this.remove(containerId)
			}
		}
	}
	
	Map createContainer(Map containerConfig) {
		def containerResponse = dockerClient.createContainer(containerConfig)
		return [id: containerResponse["content"]["Id"]]
	}
	
	void putArchive(String containerId, String dest, InputStream archiveStream) {
		dockerClient.putArchive(containerId, dest, archiveStream)
	}
	
	Map pull(String image, String tag) {
		return [id: dockerClient.pull(image, tag?:"latest")]
	}
}
