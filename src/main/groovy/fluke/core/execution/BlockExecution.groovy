package fluke.core.execution

import fluke.core.keyword.KeywordMap;

class BlockExecution implements Execution {

	KeywordMap keywordMap
	
	KeywordMap getKeywordMap() {
		return this.keywordMap
	}
}
