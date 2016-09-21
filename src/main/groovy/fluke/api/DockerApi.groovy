package fluke.api

import de.gesellix.docker.client.DockerAsyncCallback;
import de.gesellix.docker.client.DockerClient;
import de.gesellix.docker.client.DockerClientException;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.common.FlukeConsole;
import fluke.exception.DockerApiException;
import fluke.exception.OperationException;
import groovy.json.JsonSlurper;

class DockerApi {
	private static FlukeConsole console = FlukeConsole.getConsole()

	private DockerClient dockerClient = new DockerClientImpl()

	Map run(String image, Map containerConfig, boolean showLogs = false) {
		try {
			Map result = [:]
			def runResponse = dockerClient.run(image, containerConfig)
			result << [containerId: runResponse["container"]["content"]["Id"]]
			if(showLogs) {
				dockerClient.logs(result.containerId, new DockerAsyncCallback() {
							def onEvent(event) {
								console.printContainerLog(event)
							}
						})
			}
			result << this.wait(result.containerId)
			result << this.logs(result.containerId)
			return result
		} catch(DockerClientException e) {
			throw new DockerApiException("Unable to run image ${image} with command ${containerConfig.Cmd}")
		}
	}

	Map wait(String containerId) {
		try {
			def waitResponse = dockerClient.wait(containerId)
			return [statusCode: waitResponse["content"]["StatusCode"]]
		} catch(DockerClientException e) {
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
		} catch(DockerClientException e) {
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
		} catch(DockerClientException e) {
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
		} catch(DockerClientException e) {
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
		} catch(DockerClientException e) {
			throw new DockerApiException("Unable to create container")
		}
	}

	void putArchive(String containerId, String dest, InputStream archiveStream) {
		try {
			dockerClient.putArchive(containerId, dest, archiveStream)
		} catch(DockerClientException e) {
			println e?.detail?.status?.code
			println e?.detail
			if(e?.detail?.status?.code == 404) {
				throw new DockerApiException("Could not find directory ${dest}")
			} else {
				throw new DockerApiException("Unable to push file(s) to container ${containerId} in directory ${dest}")
			}
		}
	}

	Map pull(String image, tag = "", authBase64Encoded = ".") {
		try {
			def response = dockerClient.getHttpClient().post([path   : "/images/create",
				query  : [fromImage: image,
					tag      : tag,
					registry : ""],
				async : true,
				headers: ["X-Registry-Auth": "."]])
			PullProgressHandler handler = new PullProgressHandler()
			InputStreamReader reader = new InputStreamReader(response.stream)
			String line
			while((line = reader.readLine()) != null) {
				if(!line.isEmpty()) {
					def jsonSlurper = new JsonSlurper()
					handler.handle(jsonSlurper.parseText(line))
				}
			}
			return [id: dockerClient.findImageId(image, tag)]
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

	Map image(String image) {
		try{
			def response = dockerClient.inspectImage(image)
			return response.content
		} catch(DockerClientException e) {
			return false
		}
	}
}
