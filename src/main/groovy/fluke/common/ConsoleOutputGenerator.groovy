package fluke.common

trait ConsoleOutputGenerator {

	def printMessage(String message) {
		println "${message}"
	}
	
	def printCommit(String imageId) {
		println "=> ${imageId}\n"
	}
}
