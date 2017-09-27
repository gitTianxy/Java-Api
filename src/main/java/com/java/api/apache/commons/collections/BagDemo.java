package com.java.api.apache.commons.collections;

import org.apache.commons.collections.Bag;
import org.apache.commons.collections.BagUtils;
import org.apache.commons.collections.bag.HashBag;
import org.apache.commons.collections.bag.TreeBag;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;

/**
 * TODO
 * 场景
 *      需要在Collection中存放多个相同对象的拷贝，并且需要很方便的取得该对象拷贝的个数。
 * 类型
 *      共5种 (typed-, predicated-, transformed-, unmodified-, synchronized-bag), 每种分为无序和有序(sorted)两个版本
 *
 * Created by kevintian on 2017/9/26.
 */
public class BagDemo {
    public static void main(String[] args) {
        BagDemo demo = new BagDemo();
        demo.typedBag();
        demo.typedSortedBag();
    }

    void unmodifiedSortedBag() {

    }

    void unmodifiedBag() {

    }

    void transformedSortedBag() {

    }

    void transformedBag() {

    }

    void predicatedSortedBag() {

    }

    void predicatedBag() {

    }

    void syncSortedBag() {

    }

    void syncBag() {

    }

    /**
     * TypedSortedBag:
     *      not only a typed bag, but its elements(which implement Comparable) are sorted
     * input bag:
     *      the class that implements SortedBag interface
     */
    void typedSortedBag() {
        System.out.println(StringUtils.center("typed sorted bag", 50, "="));
        Bag bookBag = BagUtils.typedSortedBag(new TreeBag(), Book.class);
        Book[] books = Book.getBooks();
        for (int i=0; i<books.length; i++) {
            bookBag.add(books[i], 10*(i+1));
        }
        float totalVal = 0;
        for(Book bk : books) {
            totalVal += bk.getPrice()*bookBag.getCount(bk);
        }
        System.out.println("***total value: " + totalVal);
        Iterator<Book> itr = bookBag.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }

    }

    /**
     * TypedBag:
     *      Only objects of the specified type can be added to the bag.
     *      The validation of additions is performed via an instanceof test against a specified Class.
     * input bag:
     *      the class that implements Bag interface
     */
    void typedBag() {
        Bag bag;
        System.out.println(StringUtils.center("typed bag", 50, "="));
        Bag bookBag = BagUtils.typedBag(new HashBag(), Book.class);
        Book[] books = Book.getBooks();
        for (int i=0; i<books.length; i++) {
            bookBag.add(books[i], 10*(i+1));
        }
        float totalVal = 0;
        for(Book bk : books) {
            totalVal += bk.getPrice()*bookBag.getCount(bk);
        }
        System.out.println("***total value: " + totalVal);
        Book bk1 = books[0];
        Book bk1b = new Book(bk1.getName(), bk1.getAuthor(), bk1.getPublisher(), bk1.getYear(), bk1.getPrice());
        bookBag.add(bk1b);
        System.out.println("***bk1:" + bookBag.getCount(bk1) + ", bk1b: " + bookBag.getCount(bk1b));
        Iterator<Book> itr = bookBag.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }
}
