package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Performance;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerformanceService
{
    private final PerformanceRepository performanceRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public PerformanceService(PerformanceRepository performanceRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.performanceRepository = performanceRepository;
        this.paginationAndSortingService = paginationAndSortingService;
    }

    @Transactional
    public void savePerformance(Performance performance)
    {
        performanceRepository.save(performance);
    }

    public Page<Performance> findAllPerformances(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByDeletedFalse(pageable);
    }


    public Page<Performance> findPerformanceByMatch(Long matchId, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByMatchIdAndDeletedFalseAndMatchDeletedFalse(matchId, pageable);
    }

    public Page<Performance> findPerformanceByPlayer(Long playerId, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByPlayerIdAndDeletedFalseAndPlayerDeletedFalse(playerId, pageable);
    }

    public Page<Performance> findPerformanceByTeam(Long teamId, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByPlayerTeamIdAndDeletedFalse(teamId, pageable);
    }
}
