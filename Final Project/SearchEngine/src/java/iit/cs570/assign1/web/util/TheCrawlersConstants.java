package iit.cs570.assign1.web.util;

/**
 *
 * @author Neelesh
 */
public class TheCrawlersConstants {

    public static final String CONTENTS = "contents";
    public static final String FILE_NAME = "filename";
    public static final String FILE_PATH = "filepath";
    public static final String TITLE = "title";
    public static final int MAX_SEARCH = 100;
    public static final String INDEX_DIR_DEFAULT="D:\\CS570Project\\IndexBM25Small";
    public static final String INDEX_DIR_TFIDF = "D:\\CS570Project\\IndexTFIDFSmall";
    public static final String INDEX_DIR_BM25 = "D:\\CS570Project\\IndexBM25Small";
    public static final String SIMRANK_FILE_PATH="D:\\CS570Project\\Assignment2\\SimRankApp";
    public static final String PATH_PREFIX = "/static/";
    public static final String TEXT = "text";
    public static final String FAILURE = "failure";
    public static final String SUCCESS = "success";
    public static final String JDBC_DRIVER_PATH="com.mysql.jdbc.Driver";
    public static final String DB_USER_NAME = "root";
    public static final String DB_PASSWORD = "admin";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/TEST";
    public static final String SELECT_SIMRANK_SQL ="Select distinct doc2, simscore from simrank where doc1 = ? and simscore<>0 order by simscore desc";
    public static final String SIMRANK_COL1="DOC1";
    public static final String SIMRANK_COL2="DOC2";
    public static final String SIMRANK_COL3="SIMSCORE";
    public static final int TOP_SIM_RANK_RESULTS=50;
    public static final String ERROR_MESSAGE= "Invalid query as per Apache Lucene standard or some application error. Please correct the query or contact 'The Crawlers Team'.";

}
