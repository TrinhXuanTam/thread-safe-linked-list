package com.trinhxuantam.threadsafesinglylinkedlist.utils.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.trinhxuantam.threadsafesinglylinkedlist.utils.concurrency.LockableResource;

public class ConcurrentSinglyLinkedList<T> extends LockableResource {

    private class Node {
        private T value;

        private Optional<Node> next;

        public T getValue() {
            return this.value;
        }

        public Optional<Node> getNext() {
            return this.next;
        }

        public Node getNextNode() {
            return this.next.get();
        }

        public void setNext(Optional<Node> next) {
            this.next = next;
        }

        public void setNext(Node next) {
            this.setNext(Optional.of(next));
        }

        public boolean hasNext() {
            return this.next.isPresent();
        }

        public boolean isTail() {
            return !this.hasNext();
        }

        public Node(T value) {
            this.value = value;
            this.next = Optional.empty();
        }
    }

    private Node head;

    public ConcurrentSinglyLinkedList() {
        head = new Node(null);   
    }

    public List<T> getAll() {
        return this.withReadLock(() -> {
            if (head.isTail()) {
                return List.of();
            }
            List<T> values = new ArrayList<>();
            Node current = head.getNextNode();
            while (current.hasNext()) {
                values.add(current.getValue());
                current = current.getNextNode();
            }
            values.add(current.getValue());
            return values;
        });
    }

    public T get(int index) throws IndexOutOfBoundsException {
        return this.withReadLock(() -> {
            Node current = head;
            for (int i = 0; i <= index; i++) {
                if (!current.hasNext()) {
                    throw new IndexOutOfBoundsException(index);
                }
                current = current.getNextNode();
            }
            return current.getValue();
        });
    }

    public void push(T value) {
        this.withWriteLock(() -> {
            Node newHead = new Node(value);
            newHead.setNext(head.getNext());
            head.setNext(newHead);
        });
    }

    public void insertAfter(T value, int index) throws IndexOutOfBoundsException {
        this.withWriteLock(() -> {
            Node current = head;
            for (int i = 0; i <= index; i++) {
                if (!current.hasNext()) {
                    throw new IndexOutOfBoundsException(index);
                }
                current = current.getNextNode();
            }
    
            Node newNode = new Node(value);
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        });
    }

    public T pop() throws IllegalStateException {
        return this.withWriteLock(() -> {
            if (head.isTail()) {
                throw new IllegalStateException("List is empty");
            }
    
            Node prev = head;
            Node current = head.getNextNode();
            while (current.hasNext()) {
                prev = current;
                current = current.getNextNode();
            }
    
            prev.setNext(Optional.empty());
            return current.getValue();
        });
    }
}
