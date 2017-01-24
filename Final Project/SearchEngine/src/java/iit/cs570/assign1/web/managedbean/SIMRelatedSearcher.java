/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.managedbean;

import iit.cs570.assign1.web.util.TheCrawlersConstants;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import iit.cs570.assign1.web.util.SimRankUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.util.List;

import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;

/**
 *
 * @author Neelesh
 */
public class SIMRelatedSearcher {

    private List<SearchResult> results;
    //private int searchcount;
    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;
    IndexReader reader;

    public SIMRelatedSearcher(String indexDirectoryPath) throws IOException {
        queryParser = new QueryParser(Version.LUCENE_42, TheCrawlersConstants.TEXT, new StandardAnalyzer(Version.LUCENE_42));
        Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
        reader = DirectoryReader.open(indexDirectory);

    }

    public List<SearchResult> search(String searchQuery, String path)
            throws IOException, ParseException {

        results = new ArrayList<SearchResult>();

        Connection conn = null;
        PreparedStatement stmt = null;
        System.out.println("Query to be processed is ---->" + searchQuery);
        query = queryParser.parse(searchQuery);
        String fileName = path.replaceFirst("^" + TheCrawlersConstants.PATH_PREFIX, "");

        int id = DocumentMapBuilder.getInstance().getDocumentNameIdMap().get(fileName);
        System.out.println("Id of the file is given as: " + id);
        try {
            int count = 0;
            conn = SimRankUtil.getInstance().getConnection();
            stmt = conn.prepareStatement(TheCrawlersConstants.SELECT_SIMRANK_SQL);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next() && count < TheCrawlersConstants.TOP_SIM_RANK_RESULTS) {
                count++;
                int col = rs.getInt(TheCrawlersConstants.SIMRANK_COL2);
                float score = rs.getFloat(TheCrawlersConstants.SIMRANK_COL3);
                Document doc = reader.document(col);
                String summary = getBestFragment(doc);
                if (summary == null || "".equalsIgnoreCase(summary)) {
                    summary = doc.get(TheCrawlersConstants.TEXT);
                    if (summary != null) {
                        summary = "Query terms not found in the document. Showing content. " + summary.substring(0, 75);
                    }
                }
                if (summary != null) {
                    SearchResult res = new SearchResult(doc.get(TheCrawlersConstants.TITLE), doc.get(TheCrawlersConstants.FILE_NAME), score, summary);
                    results.add(res);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;

    }

    public void close() throws IOException {
        reader.close();

    }

    public String getBestFragment(Document doc) throws IOException, InvalidTokenOffsetsException {
        Scorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(scorer);
        return highlighter.getBestFragment(new StandardAnalyzer(Version.LUCENE_42), TheCrawlersConstants.TEXT, doc.get(TheCrawlersConstants.TEXT));
    }
}
