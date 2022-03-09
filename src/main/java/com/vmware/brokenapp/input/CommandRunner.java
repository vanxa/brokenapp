package com.vmware.brokenapp.input;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
public class CommandRunner {

    public static final String[] BLACKLISTED_COMMANDS = new String[]{
            "vim", "ls", "nc", "netcat", "telnet"
    };

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            return;
        }
        String cmd = args[0];
        // Check if we have absolute path command, like /usr/bin/vim
        if (Pattern.compile("/usr/bin/.*").matcher(cmd).matches()) {
            System.err.println("Command is in blacklisted path, not allowed");
            return;
        }
        // We don't, check for specific commands, i.e. vim
        if (ArrayUtils.contains(BLACKLISTED_COMMANDS, cmd)) {
            System.err.println("Command is blacklisted");
            return;
        }
        // OK, let's sanitize the command further, and pass it on
        Pattern cmdPattern = Pattern.compile("[^a-zA-Z0-9]", Pattern.DOTALL);
        args[0] = cmdPattern.matcher(cmd).replaceAll("");
        // Ready to run - we are running a secure command now!
        Process process = Runtime.getRuntime().exec(args);
        System.out.println(IOUtils.readLines(process.getInputStream(), Charset.defaultCharset()));
    }

}
