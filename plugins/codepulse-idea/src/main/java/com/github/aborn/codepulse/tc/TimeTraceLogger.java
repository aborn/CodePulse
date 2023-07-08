package com.github.aborn.codepulse.tc;

import com.intellij.openapi.diagnostic.LogLevel;
import com.intellij.openapi.diagnostic.Logger;

/**
 * @author aborn
 * @date 2021/02/12 11:26 PM
 */
public class TimeTraceLogger {
    public TimeTraceLogger() {
    }

    protected static final Logger LOG = Logger.getInstance(TimeTraceLogger.class);

    public static void info(String message) {
        LOG.info(" : #CodePulse : " + message);
    }

    public static void setLevel(LogLevel level) {
        LOG.setLevel(level);
    }
}
