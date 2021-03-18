package com.vmware.brokenapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class CommandOutput {

    private List<String> output;
    private String[] cmd;

    @JsonCreator
    public CommandOutput(@JsonProperty("cmd") String[] cmd) {
        this.cmd = cmd;
        try {
            this.output = new SafeCommandRunner().executeCommand(cmd);
        } catch (Exception e) {
            this.output = Collections.singletonList(e.getMessage());
        }
    }

    public List<String> getOutput() {
        return output;
    }

    public String[] getCmd() {
        return cmd;
    }
}
