package com.java.api.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.java.api.guava.cache.CacheDemo;
import com.java.api.guava.cache.EvictionStrategy;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevintian on 2017/10/13.
 */
public class CacheTest {
    CacheDemo demo;
    Map<String, Object> initMap;
    EvictionStrategy strategy; //缓存回收策略
    boolean recordStats; //缓存统计开关

    @Before
    public void before() {
        demo = new CacheDemo();
        initMap = new HashMap<>();
        int i=0;
        while (i++<10) {
            initMap.put("key_" + i, "val_" + i);
        }
        strategy = new EvictionStrategy();
        strategy.setMaxSize(20l);
        strategy.setTimeAfterAccess(1l);
        strategy.setTimeUnit(TimeUnit.MINUTES);
        recordStats = false;
    }

    @Test
    public void testCacheLoader() throws ExecutionException {
        LoadingCache<String, Object> cache = demo.initLoadingCache(initMap, strategy, recordStats);
        String key1 = "key_1";
        System.out.println(String.format("get: %s=%s", key1, cache.get(key1)));
        String key11 = "key_11";
        System.out.println(String.format("getIfPresent: %s=%s", key11, cache.getIfPresent(key11)));
        System.out.println(String.format("get: %s=%s", key11, cache.get(key11)));
        String key12 = "key_12";
        Object value12 = cache.get(key12, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println(String.format("***%s is absent, assign value to it", key12));
                return "value_assigned_2_" + key12;
            }
        });
        System.out.println(String.format("get(k, ck): %s=%s", key12, value12));
    }

    @Test
    public void testCallableCallback() throws ExecutionException {
        Cache<String, Object> cache = demo.initCache(initMap, strategy, recordStats);
        String key1 = "key_1";
        Object val1 = cache.get(key1, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println(String.format("***%s is absent, assign value to it", key1));
                return "value_assigned_2_" + key1;
            }
        });
        System.out.println(String.format("get(k, ck): %s=%s", key1, val1));
        String key11 = "key_11";
        Object val11 = cache.get(key11, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println(String.format("***%s is absent, assign value to it", key11));
                return "value_assigned_2_" + key11;
            }
        });
        System.out.println(String.format("get(k, ck): %s=%s", key11, val11));
    }

    @Test
    public void testPassiveEvict() {
        strategy.setMaxSize(15l);
        LoadingCache<String, Object> cache = demo.initLoadingCache(initMap, strategy, recordStats);
        int i=0;
        while (i++<10) {
            String key = "NEW_key_" + i;
            String value = "NEW_val_" + i;
            cache.put(key, value);
        }
        System.out.println("===final result===");
        for(Map.Entry<String, Object> entry : cache.asMap().entrySet()) {
            System.out.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
        }
    }
}
