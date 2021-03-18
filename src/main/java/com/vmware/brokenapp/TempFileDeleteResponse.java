package com.vmware.brokenapp;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class TempFileDeleteResponse {

    private String msg;

    public TempFileDeleteResponse(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
