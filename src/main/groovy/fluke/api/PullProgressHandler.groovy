package fluke.api

import java.util.Map;

import fluke.common.FlukeConsole;
import fluke.exception.DockerApiException

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
	
	def handle(Map progressResponse) {
		if(progressResponse.error) {
			handleError(progressResponse.error)
		}
		printHeader(progressResponse)
		printRow(progressResponse)
		String status = progressResponse.status
	}

	private void handleError(String error) {
		throw new DockerApiException(error)
	}
	
	private void printHeader(Map progressResponse) {
		PullStatus currentStatus = PullStatus.values().find {it.label == progressResponse.status}
		if(currentStatus && this.prevStatus != currentStatus) {
			Long totalBytes = progressResponse.progressDetail?.total
			String totalBytesStr = byteCountToDisplaySize(totalBytes)
			println("${currentStatus.label} ${totalBytesStr}: ")
			this.prevStatus = currentStatus
		}
	}

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