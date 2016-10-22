/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iitg.cs570.assign1.bm25;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Neelesh
 */
public class HTMLFileFilter implements FileFilter{
    
    @Override
   public boolean accept(File pathname) {
      return pathname.getName().toLowerCase().endsWith(".html");
   }
}
