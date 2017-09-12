package com.java.api.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * ref:
 *      1. http://www.cnblogs.com/lanxuezaipiao/p/3635556.html
 */
public interface CLibrary extends Library {
    /**
     * Generate java instance representing dll api
     *
     * Native.loadLibrary(String dll-name(case-insensitive), this-interface.class)
     */
    CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);


    /**
     * api method corresponding to that defined in dll file
     * NOTE:
     *      1. [method--name match] method-name is case sensitive
     *      2. [params--type & number match] parameter type-mapping between native and java. e.g. int<-->int, char<-->byte
     */
    void printf(String format, Object... args);
}