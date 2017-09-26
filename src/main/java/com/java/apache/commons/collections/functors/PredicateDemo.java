package com.java.apache.commons.collections.functors;

import com.java.apache.commons.collections.Book;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by kevintian on 2017/9/26.
 */
public class PredicateDemo {
    public static void main(String[] args) {
        // add conditions
        Predicate clsCondition = new InstanceofPredicate(Book.class);
        Predicate notNullCondition = NotNullPredicate.getInstance();
        Predicate valCondition = new Predicate() {
            @Override
            public boolean evaluate(Object book) {
                if (book instanceof Book) {
                    return ((Book) book).getPrice() > 10f;
                }
                return false;
            }
        };
        Predicate allConditions = PredicateUtils.allPredicate(new Predicate[]{clsCondition, notNullCondition,
                valCondition});
        // validate data
        Book[] books = Book.getBooks();
        for (Book bk : books) {
            System.out.println(StringUtils.repeat("-", 10));
            System.out.println("is " + bk + " instance of Book:" + clsCondition.evaluate(bk));
            System.out.println("is " + bk + " not null: " + notNullCondition.evaluate(bk));
            System.out.println("is " + bk + " price >10: " + valCondition.evaluate(bk));
            System.out.println("***is " + bk + " all three conditions: " + allConditions.evaluate(bk));
        }
    }
}
