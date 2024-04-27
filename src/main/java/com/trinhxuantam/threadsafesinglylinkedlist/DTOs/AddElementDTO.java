package com.trinhxuantam.threadsafesinglylinkedlist.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO for adding a new element to the linked list.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddElementDTO {
    private Integer element; // The element to add
}
