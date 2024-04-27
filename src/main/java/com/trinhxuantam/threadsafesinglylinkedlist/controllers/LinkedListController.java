package com.trinhxuantam.threadsafesinglylinkedlist.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinhxuantam.threadsafesinglylinkedlist.DTOs.AddElementDTO;
import com.trinhxuantam.threadsafesinglylinkedlist.DTOs.InsertElementDTO;
import com.trinhxuantam.threadsafesinglylinkedlist.DTOs.ListDTO;
import com.trinhxuantam.threadsafesinglylinkedlist.services.LinkedListService;

/**
 * Controller class for handling requests related to the linked list.
 */
@RestController
@RequestMapping("/api/linkedlist")
public class LinkedListController {

    @Autowired
    private LinkedListService<Integer> service; // Service for handling integer linked list operations

    /**
     * Adds an element to the end of the linked list.
     * 
     * @param element The element to be added
     * @return The updated list of elements
     */
    @PostMapping("/add")
    public ResponseEntity<ListDTO<Integer>> addElement(@RequestBody AddElementDTO dto) {
        service.append(dto.getElement());
        List<Integer> elements = service.getAllElements();
        return ResponseEntity.ok(new ListDTO<>(elements));
    }

    /**
     * Inserts an element after a specified element in the linked list.
     * 
     * @param element The element to be inserted
     * @param after   The element after which the new element should be inserted
     * @return The updated list of elements
     */
    @PostMapping("/insertAfter")
    public ResponseEntity<ListDTO<Integer>> insertAfter(@RequestBody InsertElementDTO dto) {
        service.insertAfter(dto.getElement(), dto.getAfter());
        List<Integer> elements = service.getAllElements();
        return ResponseEntity.ok(new ListDTO<>(elements));
    }

    /**
     * Removes the first occurrence of an element from the linked list.
     * 
     * @param element The element to be removed
     * @return The updated list of elements
     */
    @DeleteMapping("/pop")
    public ResponseEntity<ListDTO<Integer>> removeLast() {
        service.removeLast();
        List<Integer> elements = service.getAllElements();
        return ResponseEntity.ok(new ListDTO<>(elements));
    }

    /**
     * Removes all occurrences of an element from the linked list.
     * 
     * @param element The element to be removed
     * @return The updated list of elements
     */
    @GetMapping("/all")
    public ResponseEntity<ListDTO<Integer>> getAllElements() {
        List<Integer> elements = service.getAllElements();
        return ResponseEntity.ok(new ListDTO<>(elements));
    }
}