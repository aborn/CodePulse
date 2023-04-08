package com.github.aborn.codepulse.listeners;


import com.github.aborn.codepulse.tc.TimeTrace;
import com.github.aborn.codepulse.tc.TimeTraceLogger;

/**
 * @author aborn
 * @date 2021/02/08 3:36 PM
 */
public class UserActionBaseListener {

    protected void info(String message) {
        TimeTraceLogger.info(message);
    }

    protected void record() {
        TimeTrace.record();
    }
}
