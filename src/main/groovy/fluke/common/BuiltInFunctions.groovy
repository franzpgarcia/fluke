package fluke.common

import groovy.text.SimpleTemplateEngine
import groovy.text.Template

import java.io.InputStream;
import java.util.Map;

trait BuiltInFunctions {
	private SimpleTemplateEngine templateEngine = new SimpleTemplateEngine()
	
	InputStream template(String path, Map binding) {
		InputStream stream = new FileInputStream(path)
		return template(stream, vars)
	}

	InputStream template(InputStream stream, Map binding) {
		Template template = templateEngine.createTemplate(new BufferedReader(stream))
		return template.make(binding)
	}
	
	InputStream file(String source) {
		return new FileInputStream(source)
	}
	
	def shell = {
		cmd -> HelperFunctions.buildShellCommand(cmd)
	}
}
