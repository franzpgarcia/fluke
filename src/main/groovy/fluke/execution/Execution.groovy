package fluke.execution

import groovy.lang.MetaMethod;
import java.lang.reflect.Array;
import fluke.operation.map.OperationMap;

import org.codehaus.groovy.reflection.CachedClass;

import fluke.annotation.OperationMethod;
import fluke.exception.InvalidOperationCallException;

trait Execution {
	String blockName
	Object outer
	OperationMap operationMap
	ExecutionContext executionContext

	def methodMissing(String name, args) {
		Class opClass = this.getOperationMap()[name]
		if(opClass) {
			executeOperation(name, opClass, args)
		} else {
			this.outer.invokeMethod(name, args)
		}
	}
	
	def executeOperation(String name, Class opClass, args) {
		Object operation = opClass.newInstance(this.getExecutionContext())
		MetaMethod opMethod = findOperationMethod(operation, args)
		if(opMethod) {
			CachedClass[] parameterTypes = opMethod.getParameterTypes()
			def adaptedArgs = args
			if(parameterTypes.size() != args.size() || (parameterTypes[-1].isArray && !args[-1].class.isArray())) {
				int cutOffIndex = (parameterTypes.size() - 2)
				List tempArgList = cutOffIndex >= 0 ? args[0..cutOffIndex] : []
				tempArgList << args[(cutOffIndex + 1)..(args.size() -1)].toArray(Array.newInstance(parameterTypes[-1].theClass.getComponentType(), 0))
				adaptedArgs = tempArgList.toArray()
			}
			opMethod.invoke(operation, adaptedArgs)
		} else {
			throw new InvalidOperationCallException(name, blockName)
		}
	}

	private MetaMethod findOperationMethod(Object operation, args) {
		def validParameters = { MetaMethod m, params ->
			try {
				m.checkParameters(params); true
			} catch(e){
				false
			}
		}
		operation.metaClass.methods.find { MetaMethod m ->
			m.cachedMethod.getAnnotation(OperationMethod.class) && validParameters(m, (args.collect { a -> return a.getClass() }) as Class[])
		}
	}
}
