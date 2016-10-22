/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.bean;

import iit.cs570.assign1.index.HTMLFileFilter;
import iit.cs570.assign1.index.TFIDFIndexer;
import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import iit.cs570.assign1.index.*;
import iit.cs570.assign1.web.interfaces.*;
import java.io.File;
import org.apache.commons.io.FileUtils;
/**
 *
 * @author Neelesh
 */

@ManagedBean(name="indexerBean")
@SessionScoped
public class IndexerBean {

   private String similarity;

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }
    public IndexerBean() {
    }
    
     public String index() throws IOException
     {
         IndexerIntf indexer;
            
            if(similarity.equalsIgnoreCase("TFIDF"))
            {
            FileUtils.cleanDirectory(new File(TheCrawlersConstants.INDEX_DIR_TFIDF));
            indexer = new TFIDFIndexer(TheCrawlersConstants.INDEX_DIR_TFIDF);
            }
         
            else if(similarity.equalsIgnoreCase("BM25"))
            {
            FileUtils.cleanDirectory(new File(TheCrawlersConstants.INDEX_DIR_BM25));
            indexer = new BM25Indexer(TheCrawlersConstants.INDEX_DIR_BM25);
                
            }
            else
            {
            FileUtils.cleanDirectory(new File(TheCrawlersConstants.INDEX_DIR_TFIDF));
            indexer = new TFIDFIndexer(TheCrawlersConstants.INDEX_DIR_TFIDF);
            }
            
             
            int numIndexed;
            long startTime = System.currentTimeMillis();	
            numIndexed = indexer.createIndex(TheCrawlersConstants.DATA_DIR, new HTMLFileFilter());
            long endTime = System.currentTimeMillis();
            indexer.close();
            System.out.println(numIndexed+" File indexed, time taken: "+(endTime-startTime)+" ms");
         return "success";
     }
     
}
