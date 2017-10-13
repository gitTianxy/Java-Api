package com.java.api.guava.cache;

import com.google.common.cache.*;

import java.util.Map;

/**
 * guava cache的使用场景:
 *  1. 你预料到某些键会被查询一次以上, 并且愿意消耗一些内存空间来提升访问速度。
 *  2. 缓存中存放的数据总量不会超出内存容量。
 *  3. 本地单应用范围的缓存(若要实现夸应用缓存, 尝试Memcached这类工具)
 * guava cache操作:
 *  1. 加载
 *  2. 回收
 *  3. 访问
 *  4. 统计
 * guava cache 加载策略:
 *  1. CacheLoader: 针对整个cache的load方式--不同的key用统一的方式load
 *  2. Callable callback: 针对某个特定key的load方式--每个key可以分别指定特异的load方式
 * guava cache 回收策略:
 *  1. 被动移除(3种)
 *  2. 显式清除/主动移除
 *
 * TIPs:
 *  0. 缓存的应用领域(Guava Cache是一个全内存的本地缓存实现，它提供了线程安全的实现机制。)
 *      - CPU缓存
 *      - 操作系统同缓存
 *      - 本地缓存
 *      - 分布式缓存
 *      - HTTP缓存
 *      - 数据库缓存
 *  1. cache vs concurrentMap: Guava Cache与ConcurrentMap很相似，但也不完全一样。最基本的区别是ConcurrentMap会一直保存所有
 *  添加的元素，直到显式地移除。相对地，Guava Cache为了限制内存占用，通常都设定为自动回收元素。
 *
 * Created by kevintian on 2017/10/13.
 */
public class CacheDemo {

    /**
     * init a loading cache: load by a cacheloader
     *
     * @param map
     * @param strategy 缓存回收策略
     * @param stats 缓存统计开关
     * @return
     */
    public LoadingCache<String, Object> initLoadingCache(Map<String, Object> map, EvictionStrategy strategy, boolean stats) {
        CacheBuilder cacheBuilder = CacheBuilder.newBuilder();
        if (strategy != null) {
            if (strategy.getMaxSize() != null) {
                cacheBuilder.maximumSize(strategy.getMaxSize());
            }
            if (strategy.getTimeAfterAccess() != null) {
                cacheBuilder.expireAfterAccess(strategy.getTimeAfterAccess(), strategy.getTimeUnit());
            }
            if (strategy.getTimeAfterWrite() != null) {
                cacheBuilder.expireAfterWrite(strategy.getTimeAfterWrite(), strategy.getTimeUnit());
            }
            if (strategy.isWeakKey()) {
                cacheBuilder.weakKeys();
            }
            if (strategy.isWeakValue()) {
                cacheBuilder.weakValues();
            }
        }
        if (stats) {
            cacheBuilder.recordStats();
        }
        cacheBuilder.removalListener(new RemovalListener() {
            @Override
            public void onRemoval(RemovalNotification notification) {
                System.out.println(String.format("%s=%s 被移除: %s", notification.getKey(), notification.getValue(),
                        notification.getCause()));
            }
        });
        LoadingCache<String, Object> cache = cacheBuilder.build(new CacheLoader<String, Object>(){
            @Override
            public String load(String key) throws Exception {
                System.out.println(String.format("***%s is absent, get value from DB", key));
                String dbVal = "db_val_4_" + key;
                return dbVal;
            }
        });
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        return cache;
    }

    /**
     * init a basic cache: load by a callable callback
     *
     * @param map
     * @param strategy 缓存回收策略
     * @param stats 缓存统计开关
     * @return
     */
    public Cache<String, Object> initCache(Map<String, Object> map, EvictionStrategy strategy, boolean stats) {
        CacheBuilder cacheBuilder = CacheBuilder.newBuilder();
        if (strategy != null) {
            if (strategy.getMaxSize() != null) {
                cacheBuilder.maximumSize(strategy.getMaxSize());
            }
            if (strategy.getTimeAfterAccess() != null) {
                cacheBuilder.expireAfterAccess(strategy.getTimeAfterAccess(), strategy.getTimeUnit());
            }
            if (strategy.getTimeAfterWrite() != null) {
                cacheBuilder.expireAfterWrite(strategy.getTimeAfterWrite(), strategy.getTimeUnit());
            }
            if (strategy.isWeakKey()) {
                cacheBuilder.weakKeys();
            }
            if (strategy.isWeakValue()) {
                cacheBuilder.weakValues();
            }
        }
        if (stats) {
            cacheBuilder.recordStats();
        }
        cacheBuilder.removalListener(new RemovalListener() {
            @Override
            public void onRemoval(RemovalNotification notification) {
                System.out.println(String.format("%s=%s 被移除: %s", notification.getKey(), notification.getValue(),
                        notification.getCause()));
            }
        });
        Cache<String, Object> cache = cacheBuilder.build();
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            cache.put(entry.getKey(), entry.getValue());
        }
        return cache;
    }

    /**
     * Guava Cache提供了三种基本的缓存回收方式(被动移除)：
     *  1. 基于容量回收: CacheBuilder.maximumSize(long)
     *  2. 定时回收: expireAfterAccess(long, TimeUnit), expireAfterWrite(long, TimeUnit)
     *  3. 基于引用回收: 基于java的垃圾回收机制，根据键或者值的引用关系决定移除
     */
    public void passiveEvict() {
        // refer to 'CacheTest#testPassiveEvict'
    }

    /**
     * 显式清除/主动移除
     *  1.单独移除用 Cache.invalidate(key)
     *  2.批量移除用 Cache.invalidateAll(keys)
     *  3.移除所有用 Cache.invalidateAll()
     */
    public void activeEvict() {
        // omit...
    }

    /**
     * type:
     *  1a. get(key): add value before return
     *  1b. getIfPresent(key): does not add value
     *  2. getAll(keys)
     *  3. put(key, value)
     */
    public void visit(Cache<String, Object> cache) {
        // omit...
    }

    /**
     * show cache statistics once the function turned on
     */
    public void statistics() {
        CacheBuilder cacheBuilder = CacheBuilder.newBuilder().recordStats(); // 开启统计功能
        Cache cache = cacheBuilder.build();
        CacheStats cacheStats = cache.stats(); // get cache statistics
        cacheStats.hitRate(); //缓存命中率
        cacheStats.averageLoadPenalty(); //加载新值的平均时间，单位为纳秒
        cacheStats.evictionCount(); //缓存项被回收的总数，不包括显式清除
    }
}
