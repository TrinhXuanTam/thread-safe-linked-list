package com.trinhxuantam.threadsafesinglylinkedlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.trinhxuantam.threadsafesinglylinkedlist.utils.collections.ConcurrentSinglyLinkedList;

@SpringBootApplication
public class ThreadSafeSinglyLinkedListApplication {

	public static void main(String[] args) {
		ConcurrentSinglyLinkedList<Integer> list = new ConcurrentSinglyLinkedList<>();
		list.push(1);
		list.push(2);
		list.push(3);
		list.pop();
		list.push(4);
		list.push(5);
		list.insertAfter(99, 3);
		System.out.println(list.getAll());
		System.out.println(list.get(2));
		// SpringApplication.run(ThreadSafeSinglyLinkedListApplication.class, args);
	}

}
