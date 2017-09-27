package com.java.api.apache.commons.collections;

import org.apache.commons.collections.*;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 场景
 * 1. BidiMap: 双向Map，可以通过key找到value，也可以通过value找到key
 * 2. MultiMap: 一个key不在是简单的指向一个对象，而是一组对象
 * 3. LazyMap: Map中的键/值对一开始并不存在，当第一次被调用时才创建
 *
 *
 * Created by kevintian on 2017/9/26.
 */
public class MapDemo {
    public static void main(String[] args) throws Exception {
        Book[] books = Book.getBooks();
        MapDemo demo = new MapDemo();
        demo.bidiMap(books);
        demo.multiMap(books);
        demo.lazyMap(books);
    }

    /**
     * NOTE: BidiMap当中不光key不能重复，value也不可以。
     */
    void bidiMap(Book[] books) {
        System.out.println(StringUtils.center("bidi map", 50, "="));
        // build map
        BidiMap map = new DualHashBidiMap();
        for (Book bk : books) {
            map.put(bk.getName(), bk);
            map.put(bk.getName(), bk);
            map.put(bk.getName(), bk);
        }
        // get name, object for books[0]
        System.out.println(map.getKey(books[0]) + ":" + map.get(books[0].getName()));
        // iterate over map
        MapIterator itr = map.mapIterator();
        while (itr.hasNext()) {
            itr.next();
            System.out.println("***" + itr.getKey() + ": " + itr.getValue());
        }
    }

    /**
     * NOTE:
     * 1. factory: 每次get,如果key对应的值不存在则调用factory方法生成一个值返回,并把这个值塞入map
     * 2. transformer: ..., 调用transform方法依据input生成一个返回值, ...
     */
    void lazyMap(Book[] books) throws Exception {
        final AtomicInteger ct = new AtomicInteger();
        System.out.println(StringUtils.center("lazy map", 50, "="));
        Map map = LazyMap.decorate(new HashMap<String, Integer>(), new Factory() {
            @Override
            public Object create() {
                return ct.incrementAndGet();
            }
        });
        System.out.println(map.get("key_def_by_you") + ", map size: " + map.values().size());
        System.out.println(map.get("key_def_by_you") + ", map size: " + map.values().size());
        System.out.println(map.get("key_def_by_you1") + ", map size: " + map.values().size());
        System.out.println(map.get("key_def_by_you1") + ", map size: " + map.values().size());
        System.out.println(StringUtils.center("interate", 50, "-"));
        Iterator<Map.Entry> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry entry = itr.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println(StringUtils.repeat("-", 50));
        Map map1 = LazyMap.decorate(new HashMap<String, Integer>(), new Transformer() {
            @Override
            public Object transform(Object bookName) {
                for (Book bk : books) {
                    if (bk.getName().equals(bookName)) {
                        return bk;
                    }
                }
                return null;
            }
        });
        System.out.println(map1.get("book1") + ", map size: " + map1.values().size());
        System.out.println(map1.get("book1") + ", map size: " + map1.values().size());
        System.out.println(map1.get("book") + ", map size: " + map1.values().size());
        System.out.println(map1.get("book") + ", map size: " + map1.values().size());
        System.out.println(StringUtils.center("interate", 50, "-"));
        Iterator<Map.Entry> itr1 = map1.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry entry = itr1.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /**
     * NTOE: ...
     */
    void multiMap(Book[] books) {
        System.out.println(StringUtils.center("multi map", 50, "="));
        // build map
        MultiMap map = new MultiValueMap();
        for (Book bk : books) {
            map.put(bk.getName(), bk);
            map.put(bk.getName(), bk);
            map.put(bk.getName(), bk);
        }
        // get books[0] by name
        System.out.println(books[0].getName() + ": " + map.get(books[0].getName()));
        // iterate over map
        for (Object key : map.keySet()) {
            System.out.println("***" + key + ": " + map.get(key));
        }
    }
}
