package com.trinhxuantam.threadsafesinglylinkedlist.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO for inserting an element after another element in the linked list.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertElementDTO {
    private Integer element; // The element to insert

    private Integer after; // The element after which to insert
}