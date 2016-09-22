package fluke.docker.common

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

trait BuiltInFunctions {
	private SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()
	
	InputStream template(String path, Map binding) {
		InputStream stream = new FileInputStream(path)
		return template(stream, binding)
	}

	InputStream template(InputStream stream, Map binding) {
		Template template = templateEngine.createTemplate(new InputStreamReader(stream))
		StringWriter writer = new StringWriter()
		template.make(binding).writeTo(writer)
		return new ByteArrayInputStream(writer.toString().getBytes("UTF-8"))
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

}
