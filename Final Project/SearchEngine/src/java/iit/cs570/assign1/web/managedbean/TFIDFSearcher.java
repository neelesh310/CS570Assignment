/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.managedbean;

import iit.cs570.assign1.web.util.TheCrawlersConstants;
import iit.cs570.assign1.web.interfaces.SearcherIntf;
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
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



/**
 *
 * @author Neelesh
 */
public class TFIDFSearcher implements SearcherIntf{
    
   IndexSearcher indexSearcher;
   QueryParser queryParser;
   Query query;
   IndexReader reader;
   
   public TFIDFSearcher(String indexDirectoryPath)throws IOException{
      Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
       reader = DirectoryReader.open(indexDirectory);
       indexSearcher = new IndexSearcher(reader);
       TFIDFSimilarity tfidfSimilarity = new DefaultSimilarity();
       indexSearcher.setSimilarity(tfidfSimilarity);
        queryParser = new QueryParser(Version.LUCENE_42,
         TheCrawlersConstants.TEXT,
         new StandardAnalyzer(Version.LUCENE_42));
   }
   
   @Override
   public TopDocs search( String searchQuery) 
      throws IOException, ParseException{
      query = queryParser.parse(searchQuery);
      return indexSearcher.search(query, TheCrawlersConstants.MAX_SEARCH);
   }

      @Override
   public Document getDocument(ScoreDoc scoreDoc) 
      throws CorruptIndexException, IOException{
      return indexSearcher.doc(scoreDoc.doc);	
   }

      @Override
   public void close() throws IOException{
     reader.close();
   }
   
    @Override
   public String getBestFragment(Document doc) throws IOException, InvalidTokenOffsetsException
   {
    Scorer scorer = new QueryScorer(query);
    Highlighter highlighter = new Highlighter(scorer);
    return highlighter.getBestFragment(new StandardAnalyzer(Version.LUCENE_42), TheCrawlersConstants.TEXT, doc.get(TheCrawlersConstants.TEXT));
   }
    
}
