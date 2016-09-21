package fluke.keyword

import fluke.annotation.AllowedIn;
import fluke.annotation.Keyword;
import fluke.exception.InvalidCallException

import org.reflections.Reflections;

class KeywordMap {
	private static Map<String, Class> keywordClasses
	
	static {
		Reflections reflections = new Reflections("fluke");
		keywordClasses = reflections.getTypesAnnotatedWith(Keyword.class).collectEntries {
			[(it.getAnnotation(Keyword.class).value()): it]
		}
	}
	
	private String blockName
	
	KeywordMap(String blockName) {
		this.blockName = blockName
	}
		
	def getAt(String name) {
		Class callableClass = keywordClasses[name]
		if(callableClass) {
			String[] allowedIn = callableClass.getAnnotation(AllowedIn.class)?.value() ?:[]
			if(this.blockName in allowedIn) {
				return callableClass
			} else {
				throw new InvalidCallException(name, this.blockName)
			}
		}
		return null;
	}
}
