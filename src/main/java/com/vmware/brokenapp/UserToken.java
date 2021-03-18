package com.vmware.brokenapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class UserToken {

    private Collection<Object> permissions;
    private String token;
    private String user;

    @JsonCreator
    public UserToken(@JsonProperty("permissions") Collection<Object> permissions,
                     @JsonProperty("token") String token,
                     @JsonProperty("user") String user) {
        this.permissions = permissions;
        this.token = token;
        this.user = user;
    }

    public Collection<Object> getPermissions() {
        return permissions;
    }

    public String getToken() {
        return token;
    }

    public String getUser() {
        return user;
    }
}
