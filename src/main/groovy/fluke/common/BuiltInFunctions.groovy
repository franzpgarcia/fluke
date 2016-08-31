package fluke.common

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

import java.io.InputStream;
import java.util.Map;

trait BuiltInFunctions {
	private SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()
	
	InputStream template(String path, Map binding) {
		InputStream stream = new FileInputStream(path)
		return template(stream, binding)
	}

	InputStream template(InputStream stream, Map binding) {
		Template template = templateEngine.createTemplate(new BufferedReader(stream))
		return template.make(binding)
	}
	
	InputStream file(String source) {
		return new FileInputStream(source)
	}
	
	Map config(String source) {
		InputStream stream = new FileInputStream(source)
		return this.config(stream)
	}
	
	Map config(InputStream stream) {
		return new ConfigSlurper().parse(stream.readLines().join("\n"))
	}
	
	def shell = {
		cmd -> HelperFunctions.buildShellCommand(cmd)
	}
}
