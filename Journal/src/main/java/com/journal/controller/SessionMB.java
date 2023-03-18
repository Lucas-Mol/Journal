package com.journal.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.journal.model.User;

@ManagedBean
@SessionScoped
public class SessionMB {
	
	private static SessionMB instance;
	
	private final int startPasswordResetAttempt = 0;
	
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
    
	public void cleanAttempts() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		ExternalContext context = this.getCurrentExternalContext();
		
		Runnable attemptCleaner = () -> {
			try {
				context.getSessionMap().put("numPasswordResetAttempt", 0);
				System.out.println("Attempt cleaned");
				scheduler.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		
		scheduler.schedule(attemptCleaner, 1, TimeUnit.MINUTES);	
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

	public int getNumPasswordResetAttempt() {
		return (getAttribute("numPasswordResetAttempt") != null) ? 
				(Integer) getAttribute("numPasswordResetAttempt")
				: startPasswordResetAttempt;
	}
	
	public void setNumPasswordResetAttempt(int numPasswordResetAttempt) {
		setAttribute("numPasswordResetAttempt", numPasswordResetAttempt);
	}
  

}
