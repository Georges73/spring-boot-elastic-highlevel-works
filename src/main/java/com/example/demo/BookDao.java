package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.elasticsearch.client.RequestOptions;

@Repository
public class BookDao {
	
	
	private final String INDEX = "bookdata";
	  private final String TYPE = "books";  
	  private RestHighLevelClient restHighLevelClient;
	  private ObjectMapper objectMapper;

	  public BookDao( ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
	    this.objectMapper = objectMapper;
	    this.restHighLevelClient = restHighLevelClient;
	  }
	  
	  
	  public Book insertBook(Book book){
		  book.setId(UUID.randomUUID().toString());
		  Map dataMap = objectMapper.convertValue(book, Map.class);
		  IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, book.getId())
		                .source(dataMap);
		  try {
		    IndexResponse response = restHighLevelClient.index(indexRequest);
		  } catch(ElasticsearchException e) {
		    e.getDetailedMessage();
		  } catch (java.io.IOException ex){
		    ex.getLocalizedMessage();
		  }
		  return book;
		}
	  
	  public Map<String, Object> getBookById(String id){
		  GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
		  GetResponse getResponse = null;
		  try {
		    getResponse = restHighLevelClient.get(getRequest);
		  } catch (java.io.IOException e){
		    e.getLocalizedMessage();
		  }
		  Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
		  return sourceAsMap;
		}
	 String book = "hello";
	  
 //----------------------------------------------------------------------------------------------------------------------
	 
	 public List<Book> getBookByTitle(String title) throws IOException {

		 SearchRequest searchRequest = new SearchRequest();
	        searchRequest.indices(INDEX);
	        searchRequest.types(TYPE);

	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

	        MatchQueryBuilder matchQueryBuilder = QueryBuilders
	                .matchQuery("title",title)
	                .operator(Operator.AND);

	        searchSourceBuilder.query(matchQueryBuilder);

	        searchRequest.source(searchSourceBuilder);

	        SearchResponse searchResponse =
	        		restHighLevelClient.search(searchRequest);

	        return getSearchResult(searchResponse);
	 
		}
	  

	  
	  
	  public Map<String, Object> updateBookById(String id, Book book){
		  UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
		          .fetchSource(true);    // Fetch Object after its update
		  Map<String, Object> error = new HashMap<>();
		  error.put("Error", "Unable to update book");
		  try {
		    String bookJson = objectMapper.writeValueAsString(book);
		    updateRequest.doc(bookJson, XContentType.JSON);
		    UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
		    Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();
		    return sourceAsMap;
		  }catch (JsonMappingException e){
		    e.getMessage();
		  } catch (java.io.IOException e){
		    e.getLocalizedMessage();
		  }
		  return error;
		}
	  
	  
	  public void deleteBookById(String id) {
		  DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
		  try {
		    DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
		  } catch (java.io.IOException e){
		    e.getLocalizedMessage();
		  }
		}


	  public List<Book> findAll() throws Exception {

	        SearchRequest searchRequest = new SearchRequest();
	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
	        searchRequest.source(searchSourceBuilder);

	        SearchResponse searchResponse =
	        		restHighLevelClient.search(searchRequest);

	        return getSearchResult(searchResponse);


	    }

	  private List<Book> getSearchResult(SearchResponse response) {

	        SearchHit[] searchHit = response.getHits().getHits();

	        List<Book> book = new ArrayList<>();

	        for (SearchHit hit : searchHit){
	            book
	                    .add(objectMapper
	                            .convertValue(hit
	                                    .getSourceAsMap(), Book.class));
	        }
	        return book;
	    }


	public List<Book> wildcardQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}
	  
	
}