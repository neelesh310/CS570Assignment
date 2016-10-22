
package iitg.cs570.assign1.bm25;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;

/**
 *
 * @author Neelesh
 */
public class BM25Indexer {

private IndexWriter writer;
StandardAnalyzer analyzer;
private IndexWriterConfig config;

public BM25Indexer(String indexDirectoryPath) throws IOException {
    //this directory will contain the indexes
    Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
    analyzer = new StandardAnalyzer(Version.LUCENE_42);
    config = new IndexWriterConfig(Version.LUCENE_42, analyzer);
    config.setSimilarity(new BM25Similarity());

    //create the indexer
     writer = new IndexWriter(indexDirectory, config);
}

 public void close() throws CorruptIndexException, IOException
 {
 writer.close();
 }
 
 private Document getDocument(File file) throws IOException
    {
        Document document = new Document();
    org.jsoup.nodes.Document htmlDoc = Jsoup.parse(file, "utf-8");
    
     //index file title
     Field tileField = new Field(LuceneConstants.TITLE, htmlDoc.title(), Field.Store.YES, Field.Index.NOT_ANALYZED);
     
     Field text = new Field(LuceneConstants.TEXT, htmlDoc.text(), Field.Store.YES, Field.Index.NOT_ANALYZED); 
     
       //index file contents
        FieldType type = new FieldType();
        type.setIndexed(true);
        type.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        //type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);
        type.setStoreTermVectorOffsets(true);
        Field contentField = new Field(LuceneConstants.CONTENTS, new FileReader(file), type);
    
    //index file path
    Field filePathField = new Field(LuceneConstants.FILE_PATH, file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);
    
    //index file name
    Field fileName = new Field(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES,Field.Index.NOT_ANALYZED);
    
    document.add(tileField);
    document.add(contentField);
    document.add(filePathField);
    document.add(fileName);
    document.add(text);
    return document;
    }
 
 private void indexFile(File file) throws IOException{
      System.out.println("Indexing "+file.getCanonicalPath());
      Document document = getDocument(file);
      writer.addDocument(document);
   }

   public int createIndex(String dataDirPath, FileFilter filter) 
      throws IOException{
      //get all files in the data directory
      File[] files = new File(dataDirPath).listFiles();

      for (File file : files) {
         if(!file.isDirectory()
            && !file.isHidden()
            && file.exists()
            && file.canRead()
            && filter.accept(file)
         ){
            indexFile(file);
         }
      }
      return writer.numDocs();
   }

}
