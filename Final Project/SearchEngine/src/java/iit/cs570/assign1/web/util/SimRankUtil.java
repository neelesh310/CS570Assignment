/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.linear.SparseRealMatrix;

/**
 *
 * @author Neelesh
 */
public class SimRankUtil {
    
    private static SimRankUtil simRankUtilObj;
    private static Connection conn;
//    private static SparseRealMatrix matrix;
 
    
        
     public static SimRankUtil getInstance()
    {
        if(simRankUtilObj==null)
        {
        simRankUtilObj = new SimRankUtil();
        simRankUtilObj.buildSimRankMatrixFromFile();
        
        }
        return simRankUtilObj;
    }
     public Connection getConnection()
     {
     if(conn==null)
     {
            try
            {Class.forName(TheCrawlersConstants.JDBC_DRIVER_PATH);
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(TheCrawlersConstants.DB_URL, TheCrawlersConstants.DB_USER_NAME, TheCrawlersConstants.DB_PASSWORD);
            System.out.println("Connected database successfully...");
            }
            catch(Exception e)
            {
            e.printStackTrace();
            }
     }
     return conn;
     }
     
     public void closeConnection()
     {
      try
                {
            conn.close();
                }
            catch(Exception e)
            {
            e.printStackTrace();
            }
     }
     
     private void buildSimRankMatrixFromFile()
     {
//         BufferedReader br = null;
//         try {
//
//                    String sCurrentLine;
//                   
//                       br = new BufferedReader(new FileReader("D:\\CS570Project\\Assignment2Backup\\1.txt"));
//
//			while ((sCurrentLine = br.readLine()) != null) {
//				String[] arr = sCurrentLine.split(" ");
//                                matrix.addToEntry(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
//			}
//                        
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//                           	if (br != null)br.close();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
         
     }
    
}
