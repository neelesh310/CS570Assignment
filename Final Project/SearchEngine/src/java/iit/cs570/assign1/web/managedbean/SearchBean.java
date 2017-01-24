/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.managedbean;

import iit.cs570.assign1.web.util.TheCrawlersConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import iit.cs570.assign1.web.interfaces.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Neelesh
 */
@ManagedBean(name = "searchBean")
@RequestScoped
public class SearchBean implements Serializable {

    private String query;
    private String similarity;
    private List<SearchResult> results;
    private int searchcount;
    private String error;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public void setResults(List<SearchResult> results) {
        this.results = results;
    }

    public SearchBean() {
    }

    public String search() {
        try {
            results = new ArrayList<SearchResult>();
            // Default searcher is TFIDF
            SearcherIntf searcher = new TFIDFSearcher(TheCrawlersConstants.INDEX_DIR_BM25);

            SearchResult res;
            String summary;
            System.out.println("Similarity method is passed as: " + similarity);
            if (similarity.equalsIgnoreCase("TFIDF")) {
                searcher = new TFIDFSearcher(TheCrawlersConstants.INDEX_DIR_TFIDF);
            }

            if (similarity.equalsIgnoreCase("BM25")) {
                searcher = new BM25Searcher(TheCrawlersConstants.INDEX_DIR_BM25);
            }

            if (query == null || query.isEmpty() || query.equals("")) {
                results = null;
                searchcount = 0;
                error = TheCrawlersConstants.ERROR_MESSAGE;
                return TheCrawlersConstants.FAILURE;
            }
            TopDocs hits = searcher.search(query);

            System.out.println(hits.totalHits
                    + " documents found");

            searchcount = hits.totalHits;
            error = null;
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.getDocument(scoreDoc);
                summary = searcher.getBestFragment(doc);
                res = new SearchResult(doc.get(TheCrawlersConstants.TITLE), doc.get(TheCrawlersConstants.FILE_NAME), scoreDoc.score, summary);
                results.add(res);
            }
            if (similarity.equalsIgnoreCase("SIMBM25")) {
                List<SearchResult> results1 = new ArrayList<SearchResult>();;
                searcher = new BM25Searcher(TheCrawlersConstants.INDEX_DIR_BM25);
                TopDocs hits1 = searcher.search(query);
                for (ScoreDoc scoreDoc : hits1.scoreDocs) {
                    Document doc = searcher.getDocument(scoreDoc);
                    summary = searcher.getBestFragment(doc);
                    res = new SearchResult(doc.get(TheCrawlersConstants.TITLE), doc.get(TheCrawlersConstants.FILE_NAME), scoreDoc.score, summary);
                    results1.add(res);
                    Collections.sort(results1, Collections.reverseOrder());
                }
                SIMBM25Searcher simBM25Searcher = new SIMBM25Searcher(TheCrawlersConstants.INDEX_DIR_BM25);
                results = simBM25Searcher.search(query, results1);
                searchcount = results.size();
                simBM25Searcher.close();
            }

            Collections.sort(results, Collections.reverseOrder());
            /*for(SearchResult r:resultList)
        {
        System.out.println("Title: "+r.getTitle()+" score: "+r.getScore()+" summary: "+r.getSummary());
        }
             */

            searcher.close();

            return TheCrawlersConstants.SUCCESS;
        } catch (Exception e) {
            results = null;
            searchcount = 0;
            error = TheCrawlersConstants.ERROR_MESSAGE;
            System.out.println("Exception occured in search module--> " + e.getMessage());
            return TheCrawlersConstants.FAILURE;
        }
    }

    public String similar() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            String path = params.get("path");
            String query1 = params.get("query");
            System.out.println("The path is --> "+ path);
            SIMRelatedSearcher searcher = new SIMRelatedSearcher(TheCrawlersConstants.INDEX_DIR_BM25);
            results = searcher.search(query1, path);
            Collections.sort(results, Collections.reverseOrder());
            searchcount = results.size();
            query = query1;
            searcher.close();
        } catch (Exception e) {
            e.printStackTrace();
            results = null;
            searchcount = 0;
            error = TheCrawlersConstants.ERROR_MESSAGE;
            System.out.println("Exception occured in search module--> " + e.getMessage());
            return TheCrawlersConstants.FAILURE;
        }
        return TheCrawlersConstants.SUCCESS;
    }

}
