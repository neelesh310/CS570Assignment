/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.managedbean;

import iit.cs570.assign1.web.util.TheCrawlersConstants;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import iit.cs570.assign1.web.interfaces.*;
import iit.cs570.assign1.web.util.SimRankUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;

/**
 *
 * @author Neelesh
 */
public class SIMBM25Searcher {
    
   private List<SearchResult> results;
    //private int searchcount;
   IndexSearcher indexSearcher;
   QueryParser queryParser;
   Query query;
   IndexReader reader;
   
   
   public SIMBM25Searcher(String indexDirectoryPath)throws IOException{
        queryParser = new QueryParser(Version.LUCENE_42,TheCrawlersConstants.TEXT,new StandardAnalyzer(Version.LUCENE_42));
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
       reader = DirectoryReader.open(indexDirectory);
    
       
   }
   
  
   public List<SearchResult> search( String searchQuery, List<SearchResult> bm25Results) 
      throws IOException, ParseException{
   
        results = new ArrayList<SearchResult>();
        HashMap<Integer, Float> combinedScore= new HashMap<Integer, Float>();
        
        Connection conn = null;
        PreparedStatement stmt = null;
        System.out.println("Query to be processed is ---->"+ searchQuery);
        query = queryParser.parse(searchQuery);
      
        for(int i=0; i<5; i++)
        {
        int id = DocumentMapBuilder.getInstance().getDocumentNameIdMap().get(bm25Results.get(i).getFileName());
        float bm25Score = bm25Results.get(i).getScore();
        System.out.println("Id of the file is given as: " + id);
        System.out.println("bm25 score is : " + bm25Score);
        try {
           
            conn = SimRankUtil.getInstance().getConnection();
            stmt = conn.prepareStatement(TheCrawlersConstants.SELECT_SIMRANK_SQL);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                
                int col = rs.getInt(TheCrawlersConstants.SIMRANK_COL2);
                float score = rs.getFloat(TheCrawlersConstants.SIMRANK_COL3);                
                
                if(combinedScore.get(col)==null)
                {
                    combinedScore.put(col, bm25Score*score);
                }
                else
                {
                combinedScore.put(col, combinedScore.get(col)+(bm25Score*score));
                }

            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        
        try{
             Iterator it = combinedScore.entrySet().iterator();
            while (it.hasNext()) {
        Map.Entry<Integer, Float> pair = (Map.Entry<Integer, Float>)it.next();
        Document doc = reader.document(pair.getKey());
        String summary =  getBestFragment(doc);

        if(summary!=null)
            {
                   SearchResult res = new SearchResult(doc.get(TheCrawlersConstants.TITLE), doc.get(TheCrawlersConstants.FILE_NAME), pair.getValue(),summary);
                results.add(res);}
    }
        }
        catch(Exception e)
        {
        e.printStackTrace();
        }

        //int id = DocumentMapBuilder.getInstance().getDocumentNameIdMap().get(fileName);
        //System.out.println("Id of the file is given as: " + id);
//        try {
//            int count = 0;
//            conn = SimRankUtil.getInstance().getConnection();
//            stmt = conn.prepareStatement(TheCrawlersConstants.SELECT_SIMRANK_SQL);
//            stmt.setInt(1, id);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next() && count < TheCrawlersConstants.TOP_SIM_RANK_RESULTS) {
//                count++;
//                int col = rs.getInt(TheCrawlersConstants.SIMRANK_COL2);
//                float score = rs.getFloat(TheCrawlersConstants.SIMRANK_COL3);                
//                Document doc = reader.document(col);
//                String summary =  getBestFragment(doc);
////                if(summary==null || "".equalsIgnoreCase(summary))
////                {
////                    summary = doc.get(TheCrawlersConstants.TEXT);
////                    if(summary!=null)
////                        summary= "Query terms not found in the document. Showing content. "+summary.substring(0, 75);
////                }
//                if(summary!=null)
//                {
//                    SearchResult res = new SearchResult(doc.get(TheCrawlersConstants.TITLE), doc.get(TheCrawlersConstants.FILE_NAME), score,summary);
//                results.add(res);}
//
//            }
//          //  searchcount = results.size();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        
        return results;
    
   }


   public void close() throws IOException{
     reader.close();
   }
   
  
   public String getBestFragment(Document doc) throws IOException, InvalidTokenOffsetsException
   {
    Scorer scorer = new QueryScorer(query);
    Highlighter highlighter = new Highlighter(scorer);
    return highlighter.getBestFragment(new StandardAnalyzer(Version.LUCENE_42), TheCrawlersConstants.TEXT, doc.get(TheCrawlersConstants.TEXT));
   }
}
