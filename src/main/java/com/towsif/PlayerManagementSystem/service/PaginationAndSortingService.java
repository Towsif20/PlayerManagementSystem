package com.towsif.PlayerManagementSystem.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PaginationAndSortingService
{
    public Pageable configurePaginationAndSorting(int page,
                                                  int size,
                                                  String sortBy,
                                                  String sortOrder)
    {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);

        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.of(page, size, sort);
    }
}
