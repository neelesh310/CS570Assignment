/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iit.cs570.assign1.web.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Neelesh
 */
@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean {

    private String userName;
    private String password;
    private String correctuserName="admin";    
    private String coorectpassword="admin";
    
    public LoginBean() {
    }
    

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorrectuserName() {
        return correctuserName;
    }

    public void setCorrectuserName(String correctuserName) {
        this.correctuserName = correctuserName;
    }

    public String getCoorectpassword() {
        return coorectpassword;
    }

    public void setCoorectpassword(String coorectpassword) {
        this.coorectpassword = coorectpassword;
    }
    
    public String checkValidUser()
    {
    if(userName.equalsIgnoreCase(correctuserName))
        {
 
            if(password.equals(coorectpassword))
                return "success";
            else
            {
                return "failure";
            }
        }
        else
        {
            return "failure";
        }
    }
    
    	
    
}
