package fluke.core.execution

import java.lang.reflect.Array;

import org.codehaus.groovy.reflection.CachedClass;

import fluke.core.exception.InvalidCallException;
import fluke.core.keyword.KeywordMap;
import fluke.docker.common.BuiltInFunctions;

class BlockExecution implements Execution, BuiltInFunctions {

	KeywordMap keywordMap
	
	KeywordMap getKeywordMap() {
		return this.keywordMap
	}
}
