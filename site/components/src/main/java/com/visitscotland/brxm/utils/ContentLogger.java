package com.visitscotland.brxm.utils;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * The purpose of this logger is to encapsulate messages logged to report to the content team
 */
@Lazy
@Component
public class ContentLogger implements Logger {
    private static final Logger logger = LoggerFactory.getLogger(ContentLogger.class);;

    private final MessageCache messageCache;

    //TODO Use autowired setter in messagecache instead
    public ContentLogger(@Lazy MessageCache messageCache){
        this.messageCache = messageCache;
    }

    public String getName() {
        return logger.getName();
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public void trace(String s) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public void trace(String s, Object o) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public void trace(String s, Object o, Object o1) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public void trace(String s, Object... objects) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public void trace(String s, Throwable throwable) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    public void trace(Marker marker, String s) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public void trace(Marker marker, String s, Object o) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public void trace(Marker marker, String s, Object o, Object o1) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public void trace(Marker marker, String s, Object... objects) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public void trace(Marker marker, String s, Throwable throwable) {
        logger.error("Content Logger is not Supported on trace mode");
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void debug(String s) {
        if (messageCache.canLog(s)){
            logger.debug(s);
        }
    }

    public void debug(String s, Object o) {
        if (messageCache.canLog(s, o)){
            logger.debug(s, o);
        }
    }

    public void debug(String s, Object o, Object o1) {
        if (messageCache.canLog(s, o, o1)){
            logger.debug(s, o, o1);
        }
    }

    public void debug(String s, Object... objects) {
        if (messageCache.canLog(s, objects)){
            logger.debug(s, objects);
        }
    }

    public void debug(String s, Throwable throwable) {
        if (messageCache.canLog(s)){
            logger.debug(s, throwable);
        }
    }

    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    public void debug(Marker marker, String s) {
        if (messageCache.canLog(s)){
            logger.debug(marker, s);
        }
    }

    public void debug(Marker marker, String s, Object o) {
        if (messageCache.canLog(s, o)){
            logger.debug(marker, s, o);
        }
    }

    public void debug(Marker marker, String s, Object o, Object o1) {
        if (messageCache.canLog(s, o, o1)){
            logger.debug(marker, s, o, o1);
        }
    }

    public void debug(Marker marker, String s, Object... objects) {
        if (messageCache.canLog(s, objects)){
            logger.debug(marker, s, objects);
        }
    }

    public void debug(Marker marker, String s, Throwable throwable) {
        if (messageCache.canLog(s)){
            logger.debug(marker, s, throwable);
        }
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public void info(String s) {
        if (messageCache.canLog(s)){
            logger.info(s);
        }
    }

    public void info(String s, Object o) {
        if (messageCache.canLog(s, o)){
            logger.info(s, o);
        }
    }

    public void info(String s, Object o, Object o1) {
        if (messageCache.canLog(s, o, o1)){
            logger.info(s, o, o1);
        }
    }

    public void info(String s, Object... objects) {
        if (messageCache.canLog(s, objects)){
            logger.info(s, objects);
        }
    }

    public void info(String s, Throwable throwable) {
        if (messageCache.canLog(s)){
            logger.info(s, throwable);
        }
    }

    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    public void info(Marker marker, String s) {
        if (messageCache.canLog(s)){
            logger.info(marker, s);
        }
    }

    public void info(Marker marker, String s, Object o) {
        if (messageCache.canLog(s, o)){
            logger.info(marker, s, o);
        }
    }

    public void info(Marker marker, String s, Object o, Object o1) {
        if (messageCache.canLog(s, o, o1)){
            logger.info(marker, s, o, o1);
        }
    }

    public void info(Marker marker, String s, Object... objects) {
        if (messageCache.canLog(s, objects)){
            logger.info(marker, s, objects);
        }
    }

    public void info(Marker marker, String s, Throwable throwable) {
        if (messageCache.canLog(s)){
            logger.info(marker, s, throwable);
        }
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public void warn(String s) {
        if (messageCache.canLog(s)){
            logger.warn(s);
        }
    }

    public void warn(String s, Object o) {
        if (messageCache.canLog(s, o)){
            logger.warn(s, o);
        }
    }

    public void warn(String s, Object... objects) {
        if (messageCache.canLog(s, objects)){
            logger.warn(s, objects);
        }
    }

    public void warn(String s, Object o, Object o1) {
        if (messageCache.canLog(s, o, o1)){
            logger.warn(s, o, o1);
        }
    }

    public void warn(String s, Throwable throwable) {
        if (messageCache.canLog(s)){
            logger.warn(s, throwable);
        }
    }

    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    public void warn(Marker marker, String s) {
        if (messageCache.canLog(s)){
            logger.warn(marker, s);
        }
    }

    public void warn(Marker marker, String s, Object o) {
        if (messageCache.canLog(s, o)){
            logger.warn(marker, s, o);
        }
    }

    public void warn(Marker marker, String s, Object o, Object o1) {
        if (messageCache.canLog(s, o, o1)){
            logger.warn(marker, s, o, o1);
        }
    }

    public void warn(Marker marker, String s, Object... objects) {
        if (messageCache.canLog(s, objects)){
            logger.warn(marker, s, objects);
        }
    }

    public void warn(Marker marker, String s, Throwable throwable) {
        if (messageCache.canLog(s)){
            logger.warn(marker, s, throwable);
        }
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    public void error(String s) {
        if (messageCache.canLog(s)){
            logger.error(s);
        }
    }

    public void error(String s, Object o) {
        if (messageCache.canLog(s,o)){
            logger.error(s, o);
        }
    }

    public void error(String s, Object o, Object o1) {
        if (messageCache.canLog(s, o, o1)){
            logger.error(s, o, o1);
        }
    }

    public void error(String s, Object... objects) {
        if (messageCache.canLog(s, objects)){
            logger.error(s, objects);
        }
    }

    public void error(String s, Throwable throwable) {
        if (messageCache.canLog(s)){
            logger.error(s, throwable);
        }
    }

    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    public void error(Marker marker, String s) {
        if (messageCache.canLog(s)){
            logger.error(marker, s);
        }
    }

    public void error(Marker marker, String s, Object o) {
        if (messageCache.canLog(s, o)){
            logger.error(marker, s, o);
        }
    }

    public void error(Marker marker, String s, Object o, Object o1) {
        if (messageCache.canLog(s, o, o1)){
            logger.error(marker, s, o, o1);
        }
    }

    public void error(Marker marker, String s, Object... objects) {
        if (messageCache.canLog(s, objects)){
            logger.error(marker, s, objects);
        }
    }

    public void error(Marker marker, String s, Throwable throwable) {
        if (messageCache.canLog(s)){
            logger.error(marker, s, throwable);
        }
    }


}
