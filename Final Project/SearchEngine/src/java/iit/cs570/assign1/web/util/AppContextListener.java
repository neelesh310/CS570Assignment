/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.util;

import iit.cs570.assign1.web.managedbean.DocumentMapBuilder;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Neelesh
 */
public class AppContextListener implements ServletContextListener{
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
       
        try
        {
            System.out.println("Inside context intialization");
            System.out.println("DocumentMapBuilder.getInstance()");
        DocumentMapBuilder.getInstance();
        System.out.println("SimRankUtil.getInstance()");
        SimRankUtil.getInstance();
        }
        catch(Exception e)
        {
        
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
