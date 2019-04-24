/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195project;

/**
 *
 * @author dhoward
 */
public class User {
    
    private String userId;
    private String name;
    
    // default constructor
    public User() {    
    }
    
    // constructor with just name
    public User(String name) {
        this.userId = null;
        this.name = name;
    }
    
    // constructor with id, name
    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
