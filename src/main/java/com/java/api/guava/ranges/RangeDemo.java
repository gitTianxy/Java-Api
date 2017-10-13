package com.java.api.guava.ranges;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

/**
 * OUTLINE:
 * 1. create range of different type
 * 2. convert range 2 discrete domain
 * 3. properties of range
 * 4. element range relation
 * 5. relationship between ranges
 * 6. range operations
 * <p>
 * Created by kevintian on 2017/10/13.
 */
public class RangeDemo {

    public <C extends Comparable<?>> Range<C> createRange(C lower, C upper, RangeType type) {
        Range rg = null;
        if (type == RangeType.OPEN) {
            rg = Range.open(lower, upper); // (lower, upper)
        } else if (type == RangeType.CLOSED) {
            rg = Range.closed(lower, upper); // [lower, upper]
        } else if (type == RangeType.OPEN_CLOSED) {
            rg = Range.openClosed(lower, upper); // (lower, upper]
        } else if (type == RangeType.CLOSED_OPEN) {
            rg = Range.closedOpen(lower, upper); // [lower, upper)
        } else if (type == RangeType.AT_LEAST) {
            rg = Range.atLeast(lower); // [lower, +∞)
        } else if (type == RangeType.AT_MOST) {
            rg = Range.atMost(upper); // (-∞, upper]
        } else if (type == RangeType.GREATER_THAN) {
            rg = Range.greaterThan(lower); // (lower, +∞)
        } else if (type == RangeType.LESS_THAN) {
            rg = Range.lessThan(upper); // (-∞, upper)
        }
        return rg;
    }

    /**
     * get the set(a, a+1, a+2, ..., b) from range(a, b)
     *
     * @param range
     */
    public ContiguousSet range2DiscreteDomain(Range range) {
        ContiguousSet discreteDomain = ContiguousSet.create(range, DiscreteDomain.integers());
        return discreteDomain;
    }

    public void rangeProperties(Range range) {
        System.out.println(String.format("%s is empty: %s", range, range.isEmpty()));
        System.out.println(String.format("%s endpoints: lower=%s, upper=%s", range, range.lowerEndpoint(), range
                .upperEndpoint()));

    }

    /**
     * test if the element is contained in the range
     *
     * @param range
     * @param element
     * @param <C>
     */
    public <C extends Comparable<?>> void elementRangeRelation(Range<C> range, C element) {
        System.out.println(String.format("%s contains %s: %s", range, element, range.contains(element)));
    }

    /**
     * CONTENT
     *  1. contained
     *  2. connected
     */
    public void rangeRelations(Range range1, Range range2) {
        System.out.println(String.format("%s is contained in %s: %s", range1, range2, range1.encloses(range2)));
        System.out.println(String.format("%s is connected with %s: %s", range1, range2, range1.isConnected(range2)));
    }

    /**
     * CONTENT
     *  1. intersection
     *  2. union/span(more general)
     */
    public void rangeOperations(Range range1, Range range2) {
        Range intersection;
        try {
            intersection = range1.intersection(range2);
            System.out.println(String.format("intersection of %s and %s: %s", range1, range2, intersection));
        } catch (Exception e) {
            System.out.println(String.format("intersection of %s and %s: does not exists", range1, range2));
        }
        System.out.println(String.format("range spanned by %s and %s: %s", range1, range2, range1.span(range2)));
    }
}
