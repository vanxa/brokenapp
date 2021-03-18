package com.vmware.brokenapp;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class SafeCommandRunner {
    public static final String[] CMD_BLACKLIST = new String[]{
            "/bin/.*", "/sbin/*"
    };

    public List<String> executeCommand(String[] cmd) {
        if (ArrayUtils.isEmpty(cmd)) {
            return null;
        }
        try {

            if (isCommandBlacklisted(cmd[0])) {
                throw new IllegalArgumentException(String.format("Command %s is blacklisted", cmd[0]));
            }
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();
            if (!process.waitFor(10000, TimeUnit.MILLISECONDS)) {
                process.destroy();
                throw new RuntimeException("Command took too long to execute");
            }
            if (process.exitValue() == 0) {
                return IOUtils.readLines(process.getInputStream(), Charset.defaultCharset());
            } else {
                return IOUtils.readLines(process.getErrorStream(), Charset.defaultCharset());
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid command", e);
        }
    }

    boolean isCommandBlacklisted(String cmd) {
        for (String blacklist : CMD_BLACKLIST) {
            Pattern pattern = Pattern.compile(blacklist);
            if (pattern.matcher(cmd).matches()) {
                return true;
            }
        }
        return false;
    }
}
