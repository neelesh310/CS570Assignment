
package iit.cs570.assign1.web.managedbean;

import iit.cs570.assign1.web.util.TheCrawlersConstants;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.FSDirectory;
import iit.cs570.assign1.web.util.SimRankUtil;
/**
 *
 * @author Neelesh
 */

public class DocumentMapBuilder {
    
    IndexReader reader;
    private static DocumentMapBuilder docMapObj;
    private Map<String, Integer> documentNameIdMap; 
    private DocumentMapBuilder() throws IOException
    {
        reader = DirectoryReader.open(FSDirectory.open(new File(TheCrawlersConstants.INDEX_DIR_DEFAULT)));
    }

    
    public Map<String, Integer> getDocumentNameIdMap() {
        return documentNameIdMap;
    }

    public void setDocumentNameIdMap(Map<String, Integer> documentNameIdMap) {
        this.documentNameIdMap = documentNameIdMap;
    }
    
    public static DocumentMapBuilder getInstance() throws IOException
    {
        if(docMapObj==null)
        {
        docMapObj = new DocumentMapBuilder();
        docMapObj.buildDocumentMap();
        }
        return docMapObj;
    }
    
   @PostConstruct 
   private void buildDocumentMap ()
    {
       
       documentNameIdMap = new HashMap<String, Integer>();
    
       try{
        for(int i=0; i<reader.maxDoc();i++)
        {
        Document doc = reader.document(i);
     
        
        documentNameIdMap.put(doc.get(TheCrawlersConstants.FILE_NAME), i);
        
        }
       }
       catch(Exception e)
       {
       e.printStackTrace();
       }
        
    }
}

