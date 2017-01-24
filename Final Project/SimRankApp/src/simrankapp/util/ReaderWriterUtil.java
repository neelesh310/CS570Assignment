/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrankapp.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import org.apache.commons.math3.linear.SparseRealMatrix;

/**
 *
 * @author Neelesh
 */
public class ReaderWriterUtil {
    
public static void writeSimRankInDB(SparseRealMatrix simRankMatrix) {
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(TheCrawlersConstants.JDBC_DRIVER);
            System.out.println("Connecting to a selected database...");
            con = DriverManager.getConnection(TheCrawlersConstants.DB_URL, TheCrawlersConstants.DB_USERNAME,
                    TheCrawlersConstants.DB_PASSWORD);
            System.out.println("Connected database successfully...");
            stmt = con.prepareStatement(TheCrawlersConstants.INSERT_SIMRANK_SQL);
            int numNodes = simRankMatrix.getRowDimension();
            for (int row = 0; row < numNodes; row++) {
                for (int col = 0; col < numNodes; col++) {
                    double value = simRankMatrix.getEntry(row, col);
                    if (value > 0) {
                        stmt.setInt(1, row);
                        stmt.setInt(2, col);
                        stmt.setDouble(3, value);
                        stmt.addBatch();
                    }
                }
            }
            stmt.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
            }
        }
    }

    public static void writeSimRankToFile(SparseRealMatrix simRankMatrix, String simRankPath) {
        int numNodes = simRankMatrix.getRowDimension();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(simRankPath)));
            // row == col is always 1
            // row > col is symmetric and is equal to row < col
            for (int col = 0; col < numNodes; col++) {
                for (int row = 0; row < numNodes; row++) {
                    double value = simRankMatrix.getEntry(row, col);
                    bw.write(row + " " + col + " " + value);
                    bw.newLine();
                }
            }
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void writeSparseMatrix(SparseRealMatrix matrix, String Path)
	{
		int numNodes = matrix.getRowDimension();
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Path)));
			// row == col is always 1
			// row > col is symmetric and is equal to row < col
			for(int col=0; col < numNodes; col++)
				for(int row=0; row < numNodes; row++)		
				{
					double value = matrix.getEntry(row, col);
						bw.write(row+" "+col+" "+value);
                                                bw.newLine();
				}
			bw.close();						
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}


}


