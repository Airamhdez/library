package com.test.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.test.data.BookBean;
import com.test.util.IndexUtils;

/**
 * @author Airam Hernandez Sacramento
 */
public class BookBeanDAO {

	private static final Logger LOGGER = Logger.getLogger(BookBeanDAO.class.getName());
	
	private static final String SEARCH_INDEX = "searchIndex";

    /**
     * @return list of book beans
     */
    public List<BookBean> list(String text) {
        LOGGER.info("Retrieving list of beans");
        if(text != null && !text.isEmpty()) {
        	return findByText(text);
        } else {
        	return ObjectifyService.ofy().load().type(BookBean.class).order("id").list();
        }        
    }

    /**
     * @param id
     * @return book bean with given id
     */
    public BookBean get(Long id) {
        LOGGER.info("Retrieving bean " + id);
        return ObjectifyService.ofy().load().type(BookBean.class).id(id).now();
    }

    /**
     * Saves given bean
     * @param bean
     */
    public void save(BookBean bean) {
        if (bean == null) {
            throw new IllegalArgumentException("null book object");
        }
        LOGGER.info("Saving bean " + bean.getId());
        ObjectifyService.ofy().save().entity(bean).now();
        String tokenizeName = tokenizeString(bean.getName().toLowerCase(), ",");
        String tokenizeAuthor = tokenizeString(bean.getAuthor().toLowerCase(), ",");
        Document document = Document.newBuilder()
        			.setId(bean.getId().toString())
        			.addField(Field.newBuilder().setName("name").setText(tokenizeName))
        			.addField(Field.newBuilder().setName("author").setText(tokenizeAuthor))
                    .build();
        IndexUtils.saveDocumentToIndex(SEARCH_INDEX, document);
    }

    /**
     * Deletes given bean
     * @param bean
     */
    public void delete(BookBean bean) {
        if (bean == null) {
            throw new IllegalArgumentException("null book object");
        }
        LOGGER.info("Deleting bean " + bean.getId());
        ObjectifyService.ofy().delete().entity(bean);
        IndexUtils.deleteDocumentFromIndex(SEARCH_INDEX, bean.getId().toString());
    }
    
    /**
     * Find the books with author or name contain the text passed by parameter
     * @param text
     * @return list of book beans
     */
    private List<BookBean> findByText(String text){
    	IndexSpec indexSpec = IndexSpec.newBuilder().setName(SEARCH_INDEX).build();
        Index index = SearchServiceFactory.getSearchService().getIndex(indexSpec);
        Results<ScoredDocument> results = index.search("name = " + text.toLowerCase() + " OR author = " + text.toLowerCase());
        List<BookBean> books = new ArrayList<>();
        for (ScoredDocument document : results) {
        	Long id = Long.valueOf(document.getId());
        	books.add(get(id));
        }
        return books;
    }
    
    /**
     * Generates a tokenized string from the text passed by parameter
     * @param text
     * @param separator
     * @return
     */
    private String tokenizeString(String text, String separator) {
    	if(text != null && !text.isEmpty()) {
    		StringBuilder tokenizedText = new StringBuilder();
    		String[] textSplited = text.split(" ");
    		for(int i = 0; i < textSplited.length; i++) {
    			for(int j = 1; j <= textSplited[i].length(); j++) {
    				int k = 0;
	    			while(k + j <= textSplited[i].length()) {
	    				tokenizedText.append(textSplited[i].substring(k, k + j)).append(separator); 
	    				k++;
	    			}  				
    			}
    		}
    		return tokenizedText.substring(0, tokenizedText.length() - 1);
    	} else {
    		return "";
    	}
    }
}
