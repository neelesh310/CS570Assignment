package iit.cs570.assign1.index;

import iit.cs570.assign1.web.interfaces.IndexerIntf;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;

/**
 *
 * @author Neelesh
 */
public class TFIDFIndexer implements IndexerIntf {

    private final IndexWriter writer;
    private final StandardAnalyzer analyzer;
    private final IndexWriterConfig config;

    public TFIDFIndexer(String indexDirectoryPath) throws IOException {
        //This directory will contain the indexes
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));

        // Analyzer to analyze the document and extract the important terms
        analyzer = new StandardAnalyzer(Version.LUCENE_42);
        config = new IndexWriterConfig(Version.LUCENE_42, analyzer);

        // Default similarity in lucene is TFIDF
        TFIDFSimilarity tfIdfSimilarity = new DefaultSimilarity();

        // Configuring indexer to use TFIDF similarity
        config.setSimilarity(tfIdfSimilarity);

        //Create the indexer
        writer = new IndexWriter(indexDirectory, config);
    }

    @Override
    public void close() throws CorruptIndexException, IOException {
        writer.close();
    }

    private Document getDocument(File file) throws IOException {
        Document document = new Document();
        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(file, "utf-8");

        //Index file title
        Field tileField = new Field(TheCrawlersConstants.TITLE, htmlDoc.title(), Field.Store.YES, Field.Index.NOT_ANALYZED);
        
        //Index and store text of file
        Field text = new Field(TheCrawlersConstants.TEXT, htmlDoc.text(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);

        /*//Index file contents
        FieldType type = new FieldType();
        type.setIndexed(true);
        type.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        //type.setStored(true);
        type.setStoreTermVectors(true);
        type.setTokenized(true);
        type.setStoreTermVectorOffsets(true);
        Field contentField = new Field(TheCrawlersConstants.CONTENTS, new FileReader(file), type);*/

        //index file path
        Field filePathField = new Field(TheCrawlersConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES, Field.Index.NOT_ANALYZED);

        //index file name
        Field fileName = new Field(TheCrawlersConstants.FILE_NAME, file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED);

        document.add(tileField);
        //document.add(contentField);
        document.add(filePathField);
        document.add(text);
        document.add(fileName);

        return document;
    }

    private void indexFile(File file) throws IOException {
        System.out.println("Indexing " + file.getCanonicalPath());
        
        // Create the document from file
        Document document = getDocument(file);
        
        // Write the index for file in directory
        writer.addDocument(document);
    }

    @Override
    public int createIndex(String dataDirPath, FileFilter filter)
            throws IOException {
        //get all files in the data directory
        File[] files = new File(dataDirPath).listFiles();

        for (File file : files) {
            if (!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file)) {
                indexFile(file);
            }
        }
        return writer.numDocs();
    }

}
