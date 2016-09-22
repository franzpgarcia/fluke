package fluke.core.execution

import groovy.lang.MetaMethod;

import java.lang.reflect.Array;

import org.codehaus.groovy.reflection.CachedClass;

import fluke.core.block.BlockDefinition;
import fluke.core.exception.InvalidCallException;
import fluke.core.keyword.KeywordMap;

trait Execution {
	Object outer
	ExecutionContext executionContext
	
	def methodMissing(String name, args) {
		Class callableClass = this.getKeywordMap()[name]
		if(callableClass) {
			Object callable = callableClass.newInstance(this.getExecutionContext())
			return callable(*args)
		} else {
			def definition = getBlockDefinition(name, (args?:[]) as List)
			if(definition) {
				return definition
			} else {
				//might not work with closures
				return this.outer.invokeMethod(name, args)
			}
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

	private BlockDefinition getBlockDefinition(String name, List args) {
		List argsClasses = args.collect { it.getClass() }
		def allIn = { classes ->
			[argsClasses, classes].transpose().every {
				it[0] in it[1]
			} && argsClasses.size() == classes.size()
		}
		if(allIn([Map, Closure]) && args[0].values().every { it instanceof Class }) {
			return new BlockDefinition(name: name, params: args[0], closure: args[1])
		} else if(allIn([Closure])) {
			return new BlockDefinition(name: name, params: [:], closure: args[0])
		}
		return null
	}

	abstract KeywordMap getKeywordMap()
}
