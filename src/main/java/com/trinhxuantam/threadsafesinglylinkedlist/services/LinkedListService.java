package com.trinhxuantam.threadsafesinglylinkedlist.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.trinhxuantam.threadsafesinglylinkedlist.utils.collections.SinglyLinkedList;

/**
 * Service class for handling linked list operations.
 */
@Service
public class LinkedListService<T> {

    private final SinglyLinkedList<T> list = new SinglyLinkedList<>(); // The linked list

    /**
     * Appends an element to the end of the linked list.
     * 
     * @param element The element to be appended
     */
    public void append(T element) {
        list.push(element);
    }

    /**
     * Inserts an element after a specified element in the linked list.
     * 
     * @param element The element to be inserted
     * @param after   The element after which the new element should be inserted
     * @throws NoSuchElementException If the specified element is not found
     */
    public void insertAfter(T element, T after) throws NoSuchElementException {
        list.insertAfter(element, after);
    }

    /**
     * Removes the last element from the linked list.
     * 
     * @return The removed element
     * @throws IllegalStateException If the list is empty
     */
    public T removeLast() throws IllegalStateException {
        return list.pop();
    }

    /**
     * Gets all elements in the linked list.
     * 
     * @return The list of elements
     */
    public List<T> getAllElements() {
        return list.getAll();
    }
}