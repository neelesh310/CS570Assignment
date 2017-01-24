/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iitg.cs570.assign2.webgraph;

/**
 *
 * @author Neelesh
 */
public class CosineSimilarity {
    
    public static double CosineSimilarity(DocVector d1, DocVector d2)
    {
    double cosineSimilarity;
    try
    {
    cosineSimilarity = (d1.getVector().dotProduct(d2.getVector()))/ (d1.getVector().getNorm()*d2.getVector().getNorm());
    }
    catch(Exception e)
    {
    return 0.0;
    }
    return cosineSimilarity;
    }
}
