
package iitg.cs570.assign2.webgraph;

import simrankapp.util.TheCrawlersConstants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.util.BytesRef;

/**
 *
 * @author Neelesh
 */

// Class to generate vectors from lucene index
public class VectorGenerator {

    DocVector[] docVector;
    private Map<String, Integer> allTerms;
    Integer totalNoofDocInIndex;
    IndexReader indexReader;
    
    public VectorGenerator() throws IOException
    {
        allTerms = new HashMap<String, Integer>();
        indexReader = IndexOpener.getIndexReader();
        totalNoofDocInIndex = IndexOpener.getTotalDocInIndex();
        docVector = new DocVector[totalNoofDocInIndex];
    }
    
    public void getAllTerms() throws IOException
    {
     AllTerms allterms = new AllTerms();
     allterms.initAllTerms();
     allTerms = allterms.getAllTerms();
    }
    
    public DocVector[] getDocumentVectors() throws IOException
    {
    for(int docId=0; docId < totalNoofDocInIndex; docId++)
    {
        System.out.println("Building doc vector for doc "+docId);
    Terms vector = indexReader.getTermVector(docId, TheCrawlersConstants.TEXT);
    
    TermsEnum termsEnum=null;
    termsEnum = vector.iterator(termsEnum);
        BytesRef text = null;
        docVector[docId] = new DocVector(allTerms);
        while((text = termsEnum.next())!=null)
        {
        String term = text.utf8ToString();
        int freq = (int) termsEnum.totalTermFreq();
        docVector[docId].setEntry(term, freq);
        }
        docVector[docId].normalize();
    }
    indexReader.close();
    return docVector;
    }
}
