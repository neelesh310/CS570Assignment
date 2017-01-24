/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iitg.cs570.assign2.webgraph;

import java.util.Map;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVectorFormat;

/**
 *
 * @author Neelesh
 */
public class DocVector {
    
    private Map<String, Integer> terms;
    private OpenMapRealVector vector;

    public OpenMapRealVector getVector() {
        return vector;
    }

    public void setVector(OpenMapRealVector vector) {
        this.vector = vector;
    }
    
    public DocVector(Map<String, Integer> terms)
    {
    this.terms = terms;
    this.vector = new OpenMapRealVector(terms.size());
    }
    
    public void setEntry(String term, int freq)
    {
    if(terms.containsKey(term))
    {
    int pos = terms.get(term);
    vector.setEntry(pos, (double)freq);
    }
    }
    
    public void normalize()
    {
    double sum = vector.getL1Norm();
    vector = (OpenMapRealVector) vector.mapDivide(sum);
    }
    
    @Override
    public String toString()
    {
        RealVectorFormat formatter = new RealVectorFormat();
        return formatter.format(vector);
    }
}
