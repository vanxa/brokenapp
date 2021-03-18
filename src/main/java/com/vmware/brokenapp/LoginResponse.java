package com.vmware.brokenapp;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class LoginResponse {
    private String token;
    private String msg;

    public LoginResponse(String token, String msg) {
        this.token = token;
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public String getMsg() {
        return msg;
    }
}
