package com.java.api.apache.commons.collections;

import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Created by kevintian on 2017/9/26.
 */
@Data
public class Book implements Comparable<Book>{
    String name;
    String author;
    String publisher;
    String year;
    Float price;

    public Book() {
    }

    public Book(String name, String author, String publisher, String year, Float price) {
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.price = price;
    }

    @Override
    public int compareTo(Book o) {
        return CompareToBuilder.reflectionCompare(price, o.getPrice());
    }

    /**
     * book demos
     * @return
     */
    public static Book[] getBooks() {
        int idx = 0;
        Book bk1 = new Book("book" + (++idx), "zhang", "pb", "2017", 1f);
        Book bk2 = new Book("book" + (++idx), "li", "pb", "2017", 10f);
        Book bk3 = new Book("book" + (++idx), "wang", "pb", "2017", 100f);
        return new Book[]{bk1, bk2, bk3};
    }
}
