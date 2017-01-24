
package iitg.cs570.assign2.webgraph;

import simrankapp.util.TheCrawlersConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author Neelesh
 */
public class AllTerms {
    
    private Map<String, Integer> allTerms;
    Integer totalNumberofDocInIndex;
    IndexReader indexReader;
    
    public AllTerms() throws IOException
    {
    allTerms = new HashMap<String, Integer>();
    indexReader = IndexOpener.getIndexReader();
    totalNumberofDocInIndex = IndexOpener.getTotalDocInIndex();
    }
    
    public void initAllTerms() throws IOException
    {
    int pos=0;
    for(int docId=0; docId<totalNumberofDocInIndex; docId++)
    {
        Terms vector = indexReader.getTermVector(docId, TheCrawlersConstants.TEXT);
        TermsEnum termsEnum = null;
        termsEnum = vector.iterator(termsEnum);
        BytesRef text = null;
        while((text = termsEnum.next())!=null)
        {
        String term = text.utf8ToString();
        allTerms.put(term, pos++);
        }
    }
    
    // update position
    pos =0;
    
    for(Entry<String, Integer> s: allTerms.entrySet())
    {
        s.setValue(pos++);
    }
    }
 
    public Map<String, Integer> getAllTerms()
    {
    return allTerms;
    }
}
