package com.trinhxuantam.threadsafesinglylinkedlist.utils.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class implements a thread-safe singly linked list using atomic references to ensure
 * safe concurrent modifications without the use of explicit locks.
 * The thread safety is based on compare-and-set strategies to ensure that the list is updated
 * atomically and consistently, even when multiple threads are adding or removing elements.
 *
 * @param <T> the type of elements held in the list
 */
public class SinglyLinkedList<T> {

    /**
     * Inner class to represent a node in the linked list.
     * Each node holds an item of type T and a reference to the next node in the list,
     * wrapped in an AtomicReference for thread-safe updates.
     */
    private class Node {
        private T item;  // The item contained in the node
        private AtomicReference<Node> next;  // Atomic reference to the next node

        /**
         * Default constructor for creating a head node with no item.
         */
        public Node() {
            this.item = null;
            this.next = new AtomicReference<>(null);
        }

        /**
         * Constructs a new node with the specified item.
         * @param item the item to store in this node
         */
        public Node(T item) {
            this.item = item;
            this.next = new AtomicReference<>(null);
        }

        /**
         * Returns the item held by this node.
         * @return the item of this node
         */
        public T getItem() {
            return item;
        }

        /**
         * Returns the next node in the list.
         * @return the next node, or null if this is the last node
         */
        public Node getNext() {
            return next.get();
        }

        /**
         * Sets the next node of this node to a new node.
         * @param nextNode the new node to link as the next node
         */
        public void setNext(Node nextNode) {
            next.set(nextNode);
        }

        /**
         * Atomically sets the next node if the current next node is as expected.
         * This method is crucial for preventing race conditions in a concurrent environment.
         * @param expect the expected current next node
         * @param update the new node to link if the expected condition is met
         * @return true if successful, false otherwise
         */
        public boolean compareAndSetNext(Node expect, Node update) {
            return next.compareAndSet(expect, update);
        }
    }

    private final Node head;  // Head node of the list

    private final AtomicReference<Node> tail;  // Atomic reference to the tail node

    /**
     * Constructs a new SinglyLinkedList with a dummy head node.
     * The dummy head node simplifies edge cases like inserting the first node or removing nodes.
     */
    public SinglyLinkedList() {
        head = new Node();
        tail = new AtomicReference<>(head);
    }
    
    /**
     * Retrieves all elements from the list in sequence.
     * This method is thread-safe and can be called concurrently.
     * @return a list containing all elements in the list from head to tail
     */
    public List<T> getAll() {
        List<T> items = new ArrayList<>();
        Node current = head.getNext();
        while (current != null) {
            items.add(current.getItem());
            current = current.getNext();
        }
        return items;
    }

    /**
     * Adds a new element to the end of the list. This method uses a loop with compare-and-set
     * to ensure that the new node is correctly added even if multiple threads are adding
     * nodes simultaneously.
     * @param value the element to add
     */
    public void push(T value) {
        Node newNode = new Node(value);
        while (true) {
            Node curTail = tail.get();
            Node tailNext = curTail.getNext();
            if (curTail == tail.get()) {
                if (tailNext != null) {
                    // Advance the tail if it's behind, not pointing to the actual last node
                    tail.compareAndSet(curTail, tailNext);
                } else {
                    // Attempt to add the new node at the end of the list
                    if (curTail.compareAndSetNext(null, newNode)) {
                        // Try to update the tail to point to the new node
                        tail.compareAndSet(curTail, newNode);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Inserts a new element after a specified existing element.
     * This will iterate through the list to find the specified element and insert the new element.
     * This method throws if the specified element after which to insert does not exist.
     * 
     * @param value the new element to insert
     * @param after the element after which the new element should be inserted
     * @throws NoSuchElementException if the element 'after' is not found
     */
    public void insertAfter(T value, T after) throws NoSuchElementException {
        Node newNode = new Node(value);
        Node current = head.getNext();

        // Iterate through the list to find the 'after' element
        while (current != null) {
            // Check if the current node's item matches the 'after' item
            if (current.getItem() != null && current.getItem().equals(after)) {
                // Attempt to insert the new node after the current node
                while (true) {
                    Node nextNode = current.getNext();  // Get the next node
                    newNode.setNext(nextNode);          // Point the new node to the current node's next node
                    // Try to set the current node's next to the new node atomically
                    if (current.compareAndSetNext(nextNode, newNode)) {
                        return;  // Return if insertion is successful
                    }
                }
            }
            
            current = current.getNext();
        }

        // Throw if the 'after' element is not found in the list
        throw new NoSuchElementException("The specified element is not present in the list.");
    }

    /**
     * Removes and returns the last element from the list.
     * In case of multiple threads popping simultaneously, only one thread will succeed
     * and the second the other ones will retry until the list is updated.
     * 
     * @return the last element of the list
     * @throws IllegalStateException if the list is empty
     */
    public T pop() throws IllegalStateException {
        while (true) {
            Node last = head.getNext();  // Start with the first actual element
            if (last == null) {
                // If there are no elements, throw exception
                throw new IllegalStateException("Cannot pop from an empty list.");
            }

            Node secondLast = head;  // Start with the dummy head node
            // Iterate to find the last and second-to-last elements
            while (last.getNext() != null) {
                secondLast = last;    // Keep track of the second-to-last element
                last = last.getNext(); // Move to the next node
            }

            // Try to detach the last node, else retry
            if (secondLast.compareAndSetNext(last, null)) {
                // If successful, check if we need to reset the tail
                tail.compareAndSet(last, secondLast);
                return last.getItem(); // Return the item of the last node
            }
        }
    }
}
