/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writetodbfromfile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;

/**
 *
 * @author Neelesh
 */
public class WriteToDBFromFile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
     
        BufferedReader br = null;
         Connection con = getConnection();
        
            PreparedStatement stmt = null;
            int lines=0;
            int nonzero=0;
            int linesProcessed =0;

		try {

			String sCurrentLine;
                        
                         stmt = con.prepareStatement("INSERT INTO SIMRANK (DOC1, DOC2, SIMSCORE) VALUES (?,?,?)");

			br = new BufferedReader(new FileReader("D:\\CS570Project\\Test\\1500 File SimRank\\SimRank1.txt"));
                        
			while ((sCurrentLine = br.readLine()) != null) {
                                lines++;
                                linesProcessed++;
                                
                                if(lines<30000)
                                {
                                String[] arr = sCurrentLine.split(" ");
                                double value = Double.parseDouble(arr[2]);
                                if(value>0)
                                {
                                    nonzero++;
                                    stmt.setInt(1, Integer.parseInt(arr[0]));
                                    stmt.setInt(2, Integer.parseInt(arr[1]));
                                    stmt.setDouble(3, value);
                                    stmt.addBatch();
                                }
                        }
                                else
                                {
                                lines =0;
                                System.out.println("Lines Processed ... "+linesProcessed);
                                System.out.println("Nonzero recors ... "+nonzero);
                                stmt.executeBatch();
                                con.close();
                                con= getConnection();
                                stmt = con.prepareStatement("INSERT INTO SIMRANK (DOC1, DOC2, SIMSCORE) VALUES (?,?,?)");
                                
                                }
                               
			}
                         

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
                            stmt.close();
                            con.close();
                                 
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
    
    public static Connection getConnection() {
        Connection con = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to a selected database...");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/TEST", "root",
                    "admin");
            System.out.println("Connected database successfully...");
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    return con;
    }
}
    
    

