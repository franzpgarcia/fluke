package fluke.definition

trait Definition {
	String name
	Map params
	Closure closure
	Map defaults
	
	def defaults(Map defaults) {
		this.defaults = defaults
		return this
	}
}
