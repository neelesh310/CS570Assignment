/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iitg.cs570.assign1.tfidf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author Neelesh
 */
public class LuceneTester {

   String indexDir = "D:\\CS570Project\\IndexTFIDF";
   String dataDir = "D:\\CS570Project\\Data";
   TFIDFIndexer indexer;
   TFIDFSearcher searcher;
    public static void main(String[] args) {
        // TODO code application logic here
        
        LuceneTester tester;
      try {
         tester = new LuceneTester();
         tester.createIndex();
         tester.search("school");
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }
    }
    
    private void createIndex() throws IOException{
      indexer = new TFIDFIndexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new HTMLFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" File indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }
    
    private void search(String searchQuery) throws IOException, ParseException{
      ArrayList<SearchResult> resultList = new ArrayList<SearchResult>();
      SearchResult res=null;
      searcher = new TFIDFSearcher(indexDir);
      long startTime = System.currentTimeMillis();
      TopDocs hits = searcher.search(searchQuery);
      long endTime = System.currentTimeMillis();
   
      System.out.println(hits.totalHits +
         " documents found. Time :" + (endTime - startTime));
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
           res = new SearchResult(doc.get(LuceneConstants.TITLE), doc.get(LuceneConstants.FILE_NAME),scoreDoc.score, doc.get(LuceneConstants.TEXT));
            resultList.add(res);
      }
      
        Collections.sort(resultList, Collections.reverseOrder());
        for(SearchResult r:resultList)
        {
        System.out.println("Title: "+r.getTitle()+" score: "+r.getScore()+" path: "+r.getPath());
        }
        
      searcher.close();
   }
    
}


