package com.java.apache.commons.collections.iterators;

import com.java.apache.commons.collections.Book;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.collections.iterators.LoopingIterator;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * TODO
 *
 * 场景:
 * 0. java.util.Iterator接口定义了标准的Collection遍历方法--从头到尾一次性的遍历。
 * 1. 循环遍历
 * 2. 遍历某一段
 * 3. 遍历满足某些条件的元素
 *
 * Created by kevintian on 2017/9/26.
 */
public class IteratorDemo {
    public static void main(String[] args) {
        Book[] books = Book.getBooks();
        IteratorDemo demo = new IteratorDemo();
        demo.partialItr(books);
        demo.loopItr(Arrays.asList(books));
        demo.filterItr(Arrays.asList(books));
    }

    void partialItr(Book[] books) {
        System.out.println(StringUtils.center("partial iterate", 50, "="));
        Iterator itr = new ArrayListIterator(books, 0, 2);
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }

    void loopItr(List<Book> books) {
        System.out.println(StringUtils.center("looping iterate", 50, "="));
        Iterator itr = new LoopingIterator(books);
        int count = 10;
        while (count-->0) {
            System.out.println(itr.next());
        }
    }

    void filterItr(List<Book> books) {
        System.out.println(StringUtils.center("filter iterate", 50, "="));
        Predicate filter = new Predicate() {
            @Override
            public boolean evaluate(Object book) {
                if (book instanceof Book) {
                    return ((Book) book).getPrice() >= 10;
                }
                return false;
            }
        };
        Iterator itr = new FilterIterator(books.iterator(), filter);
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }
}
