package com.vmware.brokenapp;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class CommandRequest {
    private String[] cmd;
    private String token;

    public String[] getCmd() {
        return cmd;
    }

    public String getToken() {
        return token;
    }
}
