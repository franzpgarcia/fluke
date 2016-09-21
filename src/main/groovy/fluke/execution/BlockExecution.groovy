package fluke.execution

import java.lang.reflect.Array;

import org.codehaus.groovy.reflection.CachedClass;

import fluke.common.BuiltInFunctions;
import fluke.exception.InvalidCallException;
import fluke.keyword.KeywordMap;

class BlockExecution implements Execution, BuiltInFunctions {

	KeywordMap keywordMap
	
	KeywordMap getKeywordMap() {
		return this.keywordMap
	}
}
