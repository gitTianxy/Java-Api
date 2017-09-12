package com.java.api.jna;

import com.java.api.jna.CLibrary;
import org.junit.Test;

/**
 * Created by kevintian on 2017/9/12.
 */
public class CLibTest {

    @Test
    public void testPrintf() {
        CLibrary.INSTANCE.printf("Hello, World\n");
        String[] args = {"a", "b", "c", "d"};
        for (int i = 0; i < args.length; i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }
    }

}
