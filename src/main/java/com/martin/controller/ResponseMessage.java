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
    private final int lines;

    public ResponseMessage(String exitCode) {
        this(exitCode, 0);
    }
    public ResponseMessage(String exitCode, int lines) {
        this.exitCode=exitCode;
        this.lines = lines;
    }

    public int getLines(){
        return lines;
    }

    public String getExitCode() {
        return exitCode;
    }

}
