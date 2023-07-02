/*
 *   Copyright (c) 2023 Martin Newstead.  All Rights Reserved.
 *
 *   The author makes no representations or warranties about the suitability of the
 *   software, either express or implied, including but not limited to the
 *   implied warranties of merchantability, fitness for a particular
 *   purpose, or non-infringement. The author shall not be liable for any damages
 *   suffered by licensee as a result of using, modifying or distributing
 *   this software or its derivatives.
 */
package com.martin.controller;

public class ResponseMessage {
    private final String exitCode;

    public String getExitCode() {
        return exitCode;
    }

    public ResponseMessage(String exitCode) {
        this.exitCode=exitCode;
    }
}
