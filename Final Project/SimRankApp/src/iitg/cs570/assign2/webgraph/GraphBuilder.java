/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iitg.cs570.assign2.webgraph;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.IOException;
import simrankapp.util.TheCrawlersConstants;

/**
 *
 * @author Neelesh
 */
public class GraphBuilder {

    public static Graph buildJungGraph() throws IOException {
        Graph<Integer, String> graph = new UndirectedSparseGraph<Integer, String>();
        VectorGenerator vectorGenerator = new VectorGenerator();
        System.out.println("Get all terms in build graph");
        vectorGenerator.getAllTerms();
        System.out.println("Get doc vector in build graph");
        DocVector[] docVector = vectorGenerator.getDocumentVectors();
        int numDocs = docVector.length;       

           for (int i = 0; i < numDocs; i++) {
             graph.addVertex(i);
         }
      
        for (int i = 0; i < numDocs; i++)
        {
        graph.addEdge(i + ":" + i, i, i, EdgeType.UNDIRECTED);
        }
        for (int i = 0; i < numDocs; i++) {
            for (int j = i+1; j < numDocs; j++) {
                System.out.println("Forming edge between " + i + " and " + j);
                double cosSim = CosineSimilarity.CosineSimilarity(docVector[i], docVector[j]);
                if (cosSim >= TheCrawlersConstants.COSINE_THRESOULD) {
                    graph.addEdge(i + ":" + j, i, j, EdgeType.UNDIRECTED);
                } 
            }
        }
        //System.out.println("Write doc doc matrix");
        //ReaderWriterUtil.writeSparseMatrix(graph, "DocDocMatrix.txt");
        return graph;
    }

}
