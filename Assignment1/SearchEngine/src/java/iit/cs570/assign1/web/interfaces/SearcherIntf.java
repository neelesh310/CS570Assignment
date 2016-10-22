/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.interfaces;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

/**
 *
 * @author Neelesh
 */
public interface SearcherIntf {
    
    public TopDocs search( String searchQuery) throws IOException, ParseException;
    public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException;
    public void close() throws IOException;
    public String getBestFragment(Document doc) throws IOException, InvalidTokenOffsetsException;
    
}
