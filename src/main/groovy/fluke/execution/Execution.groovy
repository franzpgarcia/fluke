package fluke.execution

import groovy.lang.MetaMethod;

import java.lang.reflect.Array;

import fluke.keyword.KeywordMap;

import org.codehaus.groovy.reflection.CachedClass;

import fluke.exception.InvalidCallException;

trait Execution {
	String blockName
	Object outer
	ExecutionContext executionContext

	def methodMissing(String name, args) {
		Class callableClass = this.getKeywordMap()[name]
		if(callableClass) {
			Object callable = callableClass.newInstance(this.getExecutionContext())
			return callable(*args)
		} else {
			//might not work with closures
			return this.outer.invokeMethod(name, args)
		}
	}

	def propertyMissing(String name) {
		Class callableClass = this.getKeywordMap()[name]
		if(callableClass) {
			Object callable = callableClass.newInstance(this.getExecutionContext())
			return callable
		} else {
			return this.outer[name]
		}
	}
	
	abstract KeywordMap getKeywordMap()
}
