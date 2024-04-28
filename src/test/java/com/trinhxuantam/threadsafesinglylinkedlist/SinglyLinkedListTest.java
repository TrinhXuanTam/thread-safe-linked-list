package com.trinhxuantam.threadsafesinglylinkedlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trinhxuantam.threadsafesinglylinkedlist.utils.collections.SinglyLinkedList;

/**
 * Tests for the {@link SinglyLinkedList} that ensure its methods handle
 * edge cases correctly and perform actions as expected under normal conditions.
 */
public class SinglyLinkedListTest {

    private SinglyLinkedList<Integer> list;

    /**
     * Set up the test environment. This method initializes a new SinglyLinkedList
     * before each test method is executed.
     */
    @BeforeEach
    public void setUp() {
        list = new SinglyLinkedList<>();
    }

    /**
     * Tests the {@code push} method to ensure elements are added to the list
     * correctly.
     */
    @Test
    public void testPush() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);

        for (Integer integer : expected) {
            list.push(integer);
        }

        assertEquals(expected, list.getAll());
    }

    /**
     * Tests the {@code pop} method to verify it correctly removes and returns
     * the last element from the list, altering the list's state appropriately.
     */
    @Test
    public void testPop() {
        List<Integer> expected = Arrays.asList(1, 2, 3);

        for (Integer integer : expected) {
            list.push(integer);
        }

        assertEquals(3, list.pop());
        assertEquals(List.of(1, 2), list.getAll());
    }

    /**
     * Tests the {@code pop} method on an empty list to ensure it throws
     * the appropriate exception.
     */
    @Test
    public void testPopEmptyList() {
        assertThrowsExactly(IllegalStateException.class, () -> list.pop());
    }

    /**
     * Tests the {@code insertAfter} method to ensure it correctly inserts an
     * element
     * into the list after the specified existing element.
     */
    @Test
    public void testInsertAfter() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);

        for (Integer integer : expected) {
            list.push(integer);
        }

        list.insertAfter(10, 2);
        assertEquals(List.of(1, 2, 10, 3, 4, 5), list.getAll());
    }

    /**
     * Tests the {@code insertAfter} method on an empty list to ensure it throws
     * the appropriate exception when trying to insert after a non-existent element.
     */
    @Test
    public void testInsertAfterEmptyList() {
        assertThrowsExactly(NoSuchElementException.class, () -> list.insertAfter(10, 2));
    }

    /**
     * Tests the {@code insertAfter} method to ensure it throws an exception
     * when attempting to insert after an element not present in the list.
     */
    @Test
    public void testInsertAfterElementNotInList() {
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);

        for (Integer integer : expected) {
            list.push(integer);
        }

        assertThrowsExactly(NoSuchElementException.class, () -> list.insertAfter(10, 20));
        assertEquals(expected, list.getAll());
    }

    /**
     * Tests the {@code size} method to ensure it returns the correct size of the
     * list.
     */
    @Test
    public void testSize() {
        assertEquals(0, list.size());
        list.push(1);
        assertEquals(1, list.size());
        list.push(2);
        assertEquals(2, list.size());
        list.pop();
        assertEquals(1, list.size());
    }
}