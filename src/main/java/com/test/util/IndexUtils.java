package com.test.util;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

/**
 * A class of index utility
 * 
 * @author Airam
 *
 */
public class IndexUtils {
	
	private IndexUtils() {		
	}

	public static void saveDocumentToIndex(String indexName, Document document) {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		index.put(document);
	}
	
	public static void deleteDocumentFromIndex(String indexName, String id) {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
		Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
		index.delete(id);
	}
}
