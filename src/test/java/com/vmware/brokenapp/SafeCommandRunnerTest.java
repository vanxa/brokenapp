package com.vmware.brokenapp;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ivan Konstantinov (ikonstantino@vmware.com)
 */
class SafeCommandRunnerTest {

    private SafeCommandRunner runner = new SafeCommandRunner();

    @Test
    void test_runCmd_emptyCmd_returnNull() {

        assertThat(runner.executeCommand(null)).isNull();
    }

    @Test
    void test_runCmd_blacklisted_1() {
        assertThat(runner.isCommandBlacklisted("/bin/bash")).isTrue();
    }

    @Test
    void test_runCmd_blacklisted_2() {
        assertThat(runner.isCommandBlacklisted("/bin/sudo")).isTrue();
    }

    @Test
    void test_runCmd_blacklisted_3() {
        assertThat(runner.isCommandBlacklisted("/bin/rm")).isTrue();
    }

    @Test
    void test_runCmd_blacklisted_ok() {
        assertThat(runner.isCommandBlacklisted("/usr/bin/cmd")).isFalse();
    }
}
