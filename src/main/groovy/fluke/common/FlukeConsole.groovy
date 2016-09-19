package fluke.common

class FlukeConsole {
	
	private FlukeConsole() {
		
	}
	
	def printMessage(String message) {
		println "\n${message}"
	}
	
	def printCommit(String imageId) {
		println "=> ${imageId}\n"
	}

	def printContainerLog(String log) {
		println "\t>> ${log}"
	}
	
	public static FlukeConsole getConsole() {
		return new FlukeConsole()
	}
}