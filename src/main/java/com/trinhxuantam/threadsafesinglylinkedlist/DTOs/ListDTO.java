package com.trinhxuantam.threadsafesinglylinkedlist.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Encapsulates a list of items for transfer across application layers.
 *
 * @param <T> the type of elements contained in the list
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListDTO<T> {
    private List<T> data; // The list of items
}
