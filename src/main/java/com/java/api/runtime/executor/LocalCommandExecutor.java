package com.java.api.runtime.executor;

public interface LocalCommandExecutor {
    ExecuteResult executeCommand(String command, long timeout);
}
