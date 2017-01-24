/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.interfaces;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;



/**
 *
 * @author Neelesh
 */
public interface IndexerIntf
{
public void close() throws CorruptIndexException, IOException;
public int createIndex(String dataDirPath, FileFilter filter)throws IOException;
    
    
}
