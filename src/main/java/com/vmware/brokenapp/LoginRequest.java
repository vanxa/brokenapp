package com.vmware.brokenapp;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class LoginRequest {
    private String username;
    private String password;

    LoginRequest() {
        
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
