package com.journal.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.journal.model.User;

@ManagedBean
@SessionScoped
public class SessionMB {
	
	private static SessionMB instance;
	
    public static SessionMB getInstance(){
        if (instance == null){
            instance = new SessionMB();
        }
         
        return instance;
   }
    
    private ExternalContext getCurrentExternalContext(){
        if (FacesContext.getCurrentInstance() == null){
            throw new RuntimeException("O FacesContext não pode ser chamado fora de uma requisição HTTP");
        }else{
            return FacesContext.getCurrentInstance().getExternalContext();
        }
   }
    
   public User getSessionUser(){
        return (User) getAttribute("sessionUser");
   }
    
   public void setSessionUser(User user){
        setAttribute("sessionUser", user);
   }
    
   public void finishSession(){   
	   getCurrentExternalContext().invalidateSession();
   }
    
   public Object getAttribute(String name){
        return getCurrentExternalContext().getSessionMap().get(name);
   }
    
   public void setAttribute(String key, Object value){
	   getCurrentExternalContext().getSessionMap().put(key, value);
   }
   


}
