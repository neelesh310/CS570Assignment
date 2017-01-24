/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrankapp;

import edu.uci.ics.jung.graph.Graph;
import iitg.cs570.assign2.webgraph.GraphBuilder;
import simrankapp.util.TheCrawlersConstants;
import java.io.IOException;
import org.apache.commons.math3.linear.SparseRealMatrix;
import simrankapp.util.ReaderWriterUtil;

/**
 *
 * @author Neelesh
 */
public class SimRankDriverApp {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        System.out.println("Start building jung Graph");
        Graph g = GraphBuilder.buildJungGraph();
        System.out.println("Start building Sim rank  matrix");
        SparseRealMatrix simRankMatrix = SimRankAlgorithm.computeSimRank(g, TheCrawlersConstants.SIMRANK_ITER);
        System.out.println("Write Sim rank  matrix");
        ReaderWriterUtil.writeSimRankToFile(simRankMatrix, "SimRank1.txt");
        //ReaderWriterUtil.writeSimRankInDB(simRankMatrix);
    }

    
}
