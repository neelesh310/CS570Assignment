
package simrankapp.util;

/**
 *
 * @author Neelesh
 */
public class TheCrawlersConstants {
    
   public static final String CONTENTS="contents";
   public static final String FILE_NAME="filename";
   public static final String FILE_PATH="filepath";
   public static final String TITLE="title";
   public static final int MAX_SEARCH = 100;
   public static final String PATH_PREFIX ="/static/";
   public static final String DATA_DIR = "D:\\CS570Project\\DataMedium";
   public static final String INDEX_DIR_TFIDF = "D:\\CS570Project\\IndexTFIDFMedium";
   public static final String INDEX_DIR_BM25 = "D:\\CS570Project\\IndexBM25Medium";
   public static final String TEXT = "text";
   public static final String FAILURE = "failure";
   public static final String SUCCESS = "success";
   public static final int SIMRANK_ITER=5;
   public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
   public static final String DB_URL = "jdbc:mysql://localhost:3306/TEST"; 
   public static final String INSERT_SIMRANK_SQL = "INSERT INTO SIMRANK (DOC1, DOC2, SIMSCORE) VALUES (?,?,?)";
   public static final String DB_USERNAME = "root";
   public static final String DB_PASSWORD = "admin";
   public static final double COSINE_THRESOULD = 0.5;
    
}
