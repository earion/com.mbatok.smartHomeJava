package com.mbatok.sys.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CLIProcess extends Thread {
    private int result = -1;
    private String command;
    private String errorMessage = "";
    private String successMessage = "";
    private Process process;

    private Integer timeout = 7;
    private ProcessBuilder builder;


    private String executedComand;
    private List<String> commandList;
    private int secsWait = 0;

    private boolean debug = true;
    private static final Logger logger = LogManager.getLogger(CLIProcess.class);

    public CLIProcess(String command) {
        String[] listOfArguments = {"/bin/bash", "-c"};
        List<String> commandList = new ArrayList<>();
        for (String s : listOfArguments) {
            commandList.add(s);
        }
        commandList.add(command);
        builder = new ProcessBuilder(commandList);
        Map<String, String> environ = builder.environment();
        executedComand = commandList.toString();
    }

    public CLIProcess(String command, String... values) {
        this(String.format(command, values));
    }

    public CLIProcess executeWithDefaultTimeout() {
        Integer timeout = 2;
        executeWithTimeoutInSeconds(timeout);
        return this;
    }

    public CLIProcess executeWithTimeoutInSeconds(Integer n) {
        if (debug) logger.info(executedComand);
        if (debug) logger.info("Start new CLI Thread with " + n + " seconds timeout.");
        start();
        try {
            join(n * 1000);
        } catch (InterruptedException e) {
            if (debug) logger.info("CLIProcess with method: [" + executedComand + "] was interrupted.", e);
        }
        if (isAlive()) {
            interrupt();
            killProc();
        }
        return this;
    }

    public void run() {
        try {
            process = builder.start();
            process.waitFor();
            successMessage = processStream(process.getInputStream());
            errorMessage = processStream(process.getErrorStream());
            result = process.exitValue();
        } catch (IOException | InterruptedException e) {
            if (debug) logger.info("CLIProcess Thread with method: [" + executedComand + "] was interrupted.", e);
            result = -2;
            errorMessage = "timeout";
        }
    }

    private String processStream(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder bs = new StringBuilder();
        try {
            while (true) {
                String c = br.readLine();
                if (c == null)
                    break;
                bs.append(c);
                bs.append("\n");
            }
            return bs.toString().trim();
        } catch (IOException e) {
            if (debug) logger.warn("Problem while reading CLIStream input/error", e);
            return "";
        }
    }

    public int getResult() {
        return result;
    }

    public String getErrorMessage() {
        if (errorMessage != null) {
            return errorMessage;
        } else {
            return "";
        }
    }

    public String getSuccessMessage() {
        if (successMessage != null) {
            return successMessage;
        } else {
            return "";
        }
    }

    public String getCmd() {
        return executedComand;
    }

    public void killProc() {
        if (process != null) {
            process.destroy();
        }
    }

    public static void main(String[] args) {
        CLIProcess cp = new CLIProcess("dhclient eth0");
        cp.executeWithDefaultTimeout();
    }

    public static void executeCommand(String command, String errorMessageOk) throws CliProcessException {
      Logger logger = LogManager.getLogger(CLIProcess.class);

        CLIProcess cliProcess = new CLIProcess(command);
        cliProcess.executeWithTimeoutInSeconds(20);
        if (cliProcess.getResult() != 0 && !cliProcess.getErrorMessage().contains(errorMessageOk)) {
            logger.error(command + " - ERROR: " + cliProcess.getErrorMessage());
            throw new CliProcessException(CLIProcess.class.getSimpleName(),
                    "ERROR execute command: " + command + "\n" +
                            "Error message: " + cliProcess.getErrorMessage() + "\n" +
                            "Error code: " + cliProcess.getResult());
        } else {
            logger.info(command + " - SUCCESS: " + cliProcess.getSuccessMessage());
        }
    }

    public CLIProcess setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }
}