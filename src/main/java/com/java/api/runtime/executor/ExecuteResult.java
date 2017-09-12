package com.java.api.runtime.executor;

/**
 * Created by kevintian on 2017/9/12.
 */
public class ExecuteResult {
    private int exitCode;
    private String executeOut;

    public ExecuteResult(int exitCode, String executeOut) {
        this.exitCode = exitCode;
        this.executeOut = executeOut;
    }

    @Override
    public String toString() {
        return "ExecuteResult{" +
                "exitCode=" + exitCode +
                ", executeOut='" + executeOut + '\'' +
                '}';
    }
}

