package fluke.operation.map

import fluke.annotation.Operation;
import fluke.exception.InvalidOperationCallException

import org.reflections.Reflections;

class OperationMap {
	private static Set<Class> operationClasses
	
	static {
		Reflections reflections = new Reflections("fluke.operation");
		operationClasses = reflections.getTypesAnnotatedWith Operation.class
	}
	
	private String blockName
	private Set<String> activeOperations
	
	OperationMap(String blockName, String[] activeOperations) {
		this.blockName = blockName
		this.activeOperations = new HashSet<String>(Arrays.asList(activeOperations))
	}
	
	OperationMap(Collection<String> activeOperations) {
		this.activeOperations = new HashSet<String>(activeOperations)
	}
	
	def getAt(String name) {
		Class opClass = operationClasses.find {
			it.getAnnotation(Operation.class)?.value() == name
		}
		if(opClass) {
			if(name in this.activeOperations) {
				return opClass
			} else {
				throw new InvalidOperationCallException(name, this.blockName)
			}
		}
		return null;
	}
}
