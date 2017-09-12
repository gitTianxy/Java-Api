package com.java.api.script;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Note:
 * 1. 使用 Java SE 6 脚本 API，二十余种脚本语言（AppleScript、Groovy、JavaScript、Jelly、PHP、Python、Ruby 和 Velocity）
 * 都可以集成到 Java 代码中
 * 2. 脚本 API 在 Java 应用程序和外部脚本之间提供了双向可见性。Java 代码不仅可以调用外部脚本，而且还允许那些脚本访问选定的 Java 对象。
 */
public class ScriptDemo {
    static ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("JavaScript");

    static void invokePrintInJs(ScriptEngine jsEngine, String msg) throws ScriptException {
        jsEngine.eval("print('" + msg + "')");
    }

    static void defineJsFunction(ScriptEngine engine) throws ScriptException {
        // Define a function in the script engine
        engine.eval(
                "function sayHello() {" +
                        "    print('this is the js function defined in java')" +
                        "}"
        );
    }

    /**
     * invoke js function from engine
     * @param engine
     * @throws ScriptException
     */
    static void invokeJsFunctionFromEngine(ScriptEngine engine) throws ScriptException {
        engine.eval("sayHello()");
    }

    /**
     * invoke js function from java
     * @param engine
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    static void invokeJsFunctionFromJava(ScriptEngine engine) throws ScriptException,
            NoSuchMethodException {
        Invocable invocableEngine = (Invocable) engine;
        invocableEngine.invokeFunction("sayHello", "from Java");
    }

    /**
     * invoke java function from js script
     * @param engine
     * @throws ScriptException
     */
    static void invokeJavaFunctionFromJsScript(ScriptEngine engine) throws ScriptException {
        engine.put("helloScriptingWorld", new ScriptDemo());
        engine.eval(
                "print('Invoking getHelloReply method from JavaScript...');" +
                        "var msg = helloScriptingWorld.getHelloReply('JavaScript');" +
                        "print('Java returned: ' + msg)"
        );
    }

    public String getHelloReply(String name) {
        return "Java method getHelloReply says, 'Hello, " + name + "'";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Calling invokePrintInJs...");
        invokePrintInJs(jsEngine, "Hello from JavaScript");

        System.out.println("\nCalling defineJsFunction...");
        defineJsFunction(jsEngine);

        System.out.println("\nCalling invokeJsFunctionFromEngine...");
        invokeJsFunctionFromEngine(jsEngine);

        System.out.println("\nCalling invokeJsFunctionFromJava...");
        invokeJsFunctionFromJava(jsEngine);

        System.out.println("\nCalling invokeJavaFunctionFromJsScript...");
        invokeJavaFunctionFromJsScript(jsEngine);
    }
}
