package com.vmware.brokenapp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.Set;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class CredentialsFixture {

    public enum Permission {
        READ,
        WRITE,
        READ_WRITE
    }

    private Map<String, Credential> credentials;

    public Map<String, Credential> getCredentials() {
        return credentials;
    }

    @JsonIgnore
    public Credential getCredentialForUser(String username) {
        return credentials.get(username);
    }

    @JsonIgnore
    public boolean userExists(String username) {
        return credentials.containsKey(username);
    }

    public static class Credential {
        private String user;
        private String password;
        private Set<Object> permissions;

        public String getUser() {
            return user;
        }

        public String getPassword() {
            return password;
        }

        public Set<Object> getPermissions() {
            return permissions;
        }
    }
}
