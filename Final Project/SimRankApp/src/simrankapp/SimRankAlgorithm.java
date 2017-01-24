/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simrankapp;

import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import simrankapp.util.ReaderWriterUtil;

/**
 *
 * @author Neelesh
 */
public class SimRankAlgorithm {
    
    private static final float DEFAULT_C = 0.9f;
    

public SimRankAlgorithm(SparseRealMatrix graph)
{

}

public static <V> SparseRealMatrix computeSimRank(Graph<V, ?> g, int maxIter)
{
    System.out.println("Inside compute simrank");
        int numNodes = g.getVertexCount();
        SparseRealMatrix currentR = computeInitialSimRank(g);
        SparseRealMatrix nextR = new OpenMapRealMatrix(numNodes, numNodes);
        int[][] status = new int[numNodes][numNodes];
        nextR = currentR;
        for (int k=0; k<maxIter && maxIter>0; k++)
        {
            for(int l=0; l<numNodes;l++)
            {
            for(int m=0; m<numNodes;m++)
            {
                if(l==m)
                    status[l][m]=1;
                status[l][m]=0; // Unprocessed
            }
            }
            for (V a : g.getVertices())
            {
                for (V b : g.getVertices())
                {
                     int i = Integer.valueOf(String.valueOf(a));
                    int j =Integer.valueOf(String.valueOf(b));
                    if(status[i][j]==0)
                    {
                    if(i!=j)
                    {
                    float sum = computeSum(g, a, b, currentR);
                    
                    int sia = g.inDegree(a);
                    int sib = g.inDegree(b);
                   
//                    if(i==j)
//                        continue;
                    System.out.println("Inside compute simrank : computing for "+i+" and "+j);
                    if (sia == 0 || sib == 0)
                    {
                        nextR.setEntry(i, j, 0.0f);
                        nextR.setEntry(j, i, 0.0f);
                    }
                    else
                    {
                        nextR.setEntry(i, j, (DEFAULT_C / (sia * sib)) * sum);
                        nextR.setEntry(j, i, (DEFAULT_C / (sia * sib)) * sum);
                    }
                    }
                }
                    status[i][j]=1;
                    status[j][i]=1;
                }
            }

            //System.out.println("After iteration "+k);
            //print(g, nextR);

           currentR = nextR;
        }
        return currentR;
}

/**
     * Compute the sum of all SimRank values of all incoming
     * neighbors of the given vertices
     
     */
    private static <V> float computeSum(
        Graph<V,?> g, V a, V b, SparseRealMatrix simRank)
    {
        Collection<V> ia = g.getPredecessors(a);
        Collection<V> ib = g.getPredecessors(b);
        float sum = 0;
        for (V iia : ia)
        {
            for (V ijb : ib)
            {
                 int i = Integer.valueOf(String.valueOf(iia));
                 int j =Integer.valueOf(String.valueOf(ijb));
                Float r = (float)simRank.getEntry(i, j);
                sum += r;
            }
        }
        return sum;
    }

    /**
     * Compute the initial SimRank for the vertices of the given graph.
     * This initial SimRank for two vertices (a,b) is 0.0f when
     * a != b, and 1.0f when a == b
     */
    private static <V> SparseRealMatrix computeInitialSimRank(Graph<V,?> g)
    {
    System.out.println("Inside compute initial simrank");
        int numNodes = g.getVertexCount();
        SparseRealMatrix initialSimRankMatrix = new OpenMapRealMatrix(numNodes, numNodes);
        for (V a : g.getVertices())
        {
            for (V b : g.getVertices())
            {
                int i = Integer.valueOf(String.valueOf(a));
                 int j =Integer.valueOf(String.valueOf(b));
               
                if (a.equals(b))
                {
                    initialSimRankMatrix.setEntry(i, j, 1.0f);
                }
                else
                {
                    initialSimRankMatrix.setEntry(i, j, 0.0f);
                }
            }
        }
      //  ReaderWriterUtil.writeSparseMatrix(initialSimRankMatrix, "InitialSimRank.txt");
        return initialSimRankMatrix;
    }
    
    
}
