/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import iit.cs570.assign1.web.interfaces.*;
import javax.faces.bean.RequestScoped;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.util.Version;

/**
 *
 * @author Neelesh
 */
@ManagedBean(name="searchBean")
@RequestScoped
public class SearchBean {

    private String query;
    private String similarity;
    private List<SearchResult> results;
    private int searchcount;

    public int getSearchcount() {
        return searchcount;
    }

    public void setSearchcount(int searchcount) {
        this.searchcount = searchcount;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
    }
    
    public SearchBean() {
    }
    
    public String search() throws IOException, ParseException, InvalidTokenOffsetsException
    {      
    results = new ArrayList<SearchResult>();
    // Default searcher is TFIDF
      SearcherIntf searcher = new TFIDFSearcher(TheCrawlersConstants.INDEX_DIR_TFIDF);
      
      SearchResult res=null;
      String summary = null;
      System.out.println("Similarity method is passed as: "+similarity);
      if(similarity.equalsIgnoreCase("TFIDF"))
      searcher = new TFIDFSearcher(TheCrawlersConstants.INDEX_DIR_TFIDF);
      
       if(similarity.equalsIgnoreCase("BM25"))
      searcher = new BM25Searcher(TheCrawlersConstants.INDEX_DIR_BM25);
      
       if(query==null || query.isEmpty() || query.equals(""))
       {    
          results = null;
          return "failure";
       }   
        TopDocs hits = searcher.search(query);
      
      System.out.println(hits.totalHits +
         " documents found");
      
      searchcount = hits.totalHits;
     
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
      Document doc = searcher.getDocument(scoreDoc);
      summary = searcher.getBestFragment(doc);
           res = new SearchResult(doc.get(TheCrawlersConstants.TITLE), doc.get(TheCrawlersConstants.FILE_NAME),scoreDoc.score, summary);
            results.add(res);
      }
      
        Collections.sort(results, Collections.reverseOrder());
        /*for(SearchResult r:resultList)
        {
        System.out.println("Title: "+r.getTitle()+" score: "+r.getScore()+" summary: "+r.getSummary());
        }
        */
      searcher.close();
   
      return "success";
    }
    
}
