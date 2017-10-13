package com.java.api.guava;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.Range;
import com.java.api.guava.ranges.RangeDemo;
import com.java.api.guava.ranges.RangeType;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kevintian on 2017/10/13.
 */
public class RangesTest {
    RangeDemo demo;
    @Before
    public void before() {
        demo = new RangeDemo();
    }

    @Test
    public void testCreateRange() {
        Range openedRange = demo.createRange(1, 10, RangeType.OPEN);
        System.out.println(String.format("%s contains %s: %s", openedRange, 5, openedRange.contains(5)));
    }

    @Test
    public void testRange2DiscreteDomain() {
        Range closedRange = demo.createRange(1, 10, RangeType.CLOSED);
        ContiguousSet discreteDomain = demo.range2DiscreteDomain(closedRange);
        System.out.println(String.format("get set from range(%s): %s", closedRange, discreteDomain.asList()));
    }

    @Test
    public void testElementRangeRelation() {
        Range closedOpenRange = demo.createRange(1, 10, RangeType.CLOSED_OPEN);
        int element = 10;
        demo.elementRangeRelation(closedOpenRange, element);
    }

    @Test
    public void testRangeRelations() {
        Range openedRange = demo.createRange(1, 5, RangeType.OPEN);
        Range closedRange = demo.createRange(4, 10, RangeType.CLOSED);
        demo.rangeRelations(openedRange, closedRange);
    }

    @Test
    public void testRangeOperations() {
        Range openedRange = demo.createRange(1, 5, RangeType.OPEN);
        Range closedRange = demo.createRange(4, 10, RangeType.CLOSED);
        demo.rangeOperations(openedRange, closedRange);
    }
}
