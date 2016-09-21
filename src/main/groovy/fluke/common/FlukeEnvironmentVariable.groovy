package fluke.common

enum FlukeEnvironmentVariable {
	
	FLUKE_ENABLE_STACKTRACE;

	String get() {
		return System.getenv(this.toString())
	}
	
	Boolean getAsBool() {
		return Boolean.valueOf(this.get())
	}
	
	Integer getAsInt() {
		return Integer.valueOf(this.get())	
	}
}
