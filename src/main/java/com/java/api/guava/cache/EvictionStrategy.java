package com.java.api.guava.cache;

import java.util.concurrent.TimeUnit;

/**
 * Created by kevintian on 2017/10/13.
 */
public class EvictionStrategy {
    Long maxSize;
    Long timeAfterAccess;
    Long timeAfterWrite;
    TimeUnit timeUnit;
    boolean isWeakKey;
    boolean isWeakValue;

    public Long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Long maxSize) {
        this.maxSize = maxSize;
    }

    public Long getTimeAfterAccess() {
        return timeAfterAccess;
    }

    public void setTimeAfterAccess(Long timeAfterAccess) {
        this.timeAfterAccess = timeAfterAccess;
    }

    public Long getTimeAfterWrite() {
        return timeAfterWrite;
    }

    public void setTimeAfterWrite(Long timeAfterWrite) {
        this.timeAfterWrite = timeAfterWrite;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean isWeakKey() {
        return isWeakKey;
    }

    public void setWeakKey(boolean weakKey) {
        isWeakKey = weakKey;
    }

    public boolean isWeakValue() {
        return isWeakValue;
    }

    public void setWeakValue(boolean weakValue) {
        isWeakValue = weakValue;
    }
}
