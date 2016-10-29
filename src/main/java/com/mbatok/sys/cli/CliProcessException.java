package com.mbatok.sys.cli;

/**
 * Created by mateusz on 29.10.16.
 */
public class CliProcessException extends Exception {
    public CliProcessException(String simpleName, String s) {
        super(simpleName + " " + s);
    }
}
