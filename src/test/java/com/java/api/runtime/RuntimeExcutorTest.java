package com.java.api.runtime;

import com.java.api.runtime.executor.ExecuteResult;
import com.java.api.runtime.executor.LocalCommandExecutor;
import com.java.api.runtime.executor.LocalCommandExecutorImpl;
import org.junit.Test;

/**
 * Created by kevintian on 2017/9/12.
 */
public class RuntimeExcutorTest {

    @Test
    public void excuteCmd() {
        LocalCommandExecutor cmdExec = new LocalCommandExecutorImpl();
        String videoName = "\"E:/00workspace/work materials/鬼镜头(预告片).mpeg\"";
        String cmd = "MediaInfo.exe --inform=\"Video;%DisplayAspectRatio%\" ";
        ExecuteResult result = cmdExec.executeCommand(cmd + videoName, 1000);
        System.out.println(result);
    }

}
