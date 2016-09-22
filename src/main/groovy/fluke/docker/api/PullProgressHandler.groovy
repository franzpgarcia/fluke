package fluke.docker.api

import java.util.Map;

import fluke.core.common.FlukeConsole;
import fluke.docker.exception.DockerApiException;

class PullProgressHandler {
	enum PullStatus {
		DOWNLOADING("Downloading"),
		EXTRACTING("Extracting");

		String label

		PullStatus(String label) {
			this.label = label
		}
	}

	private static FlukeConsole console = FlukeConsole.getConsole()
	private static String DIGEST_PREFIX = "Digest: "
	
	private PullStatus prevStatus
	private LinkedHashMap displayedProgress = [Downloading: [:], Extracting: [:]]
	
	def handle(Map progressResponse) {
		if(progressResponse.error) {
			handleError(progressResponse.error)
		}
		handleProgress(progressResponse)
		//println(displayedProgress)
		printHeader(progressResponse)
		printRow(progressResponse)
	}

	private void handleError(String error) {
		throw new DockerApiException(error)
	}
	
	private void handleProgress(Map progressResponse) {
		if(progressResponse.progress) {
			String id = progressResponse.id
			String status = progressResponse.status
			String progress = progressResponse.progress
			this.displayedProgress[status][id] = progress
		}
	}
	
	//TODO Header only prints once for downloading (Maybe show on same line)
	private void printHeader(Map progressResponse) {
		PullStatus currentStatus = PullStatus.values().find {it.label == progressResponse.status}
		if(currentStatus && this.prevStatus != currentStatus) {
			Long totalBytes = progressResponse.progressDetail?.total
			String totalBytesStr = byteCountToDisplaySize(totalBytes)
			println("${currentStatus.label} ${totalBytesStr}: ")
			this.prevStatus = currentStatus
		}
	}

	//TODO
	private void printRow(Map progressResponse) {
		String id = progressResponse.id
		String str = progressResponse.progress ?: progressResponse.status
		println(id ? "${id}: ${str}" : "${str}")
	}

	private static String byteCountToDisplaySize(long bytes) {
		int unit = 1000;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = "KMGTPE".charAt(exp-1).toString();
		return String.format("%.2f %sB", bytes / Math.pow(unit, exp), pre);
	}
}