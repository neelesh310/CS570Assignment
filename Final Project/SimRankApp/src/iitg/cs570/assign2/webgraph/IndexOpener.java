/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iitg.cs570.assign2.webgraph;

import simrankapp.util.TheCrawlersConstants;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Neelesh
 */
public class IndexOpener {
    
    
    public static IndexReader getIndexReader() throws IOException
    {
    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(TheCrawlersConstants.INDEX_DIR_BM25)));
    return reader;
    }
    
    // Returns totl number of documents in index
    public static int getTotalDocInIndex() throws IOException
    {
    int maxDoc = getIndexReader().maxDoc();
    getIndexReader().close();
    return maxDoc;
    }
    
}
