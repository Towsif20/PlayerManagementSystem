package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Performance;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerformanceService
{
    private final PerformanceRepository performanceRepository;

    private final MatchRepository matchRepository;

    private final PlayerRepository playerRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public PerformanceService(PerformanceRepository performanceRepository, MatchRepository matchRepository, PlayerRepository playerRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.performanceRepository = performanceRepository;
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
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

    public Performance findPerformanceById(Long id)
    {
        return performanceRepository.findPerformanceByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Performance not found with id " + id));
    }

    @Transactional
    public void deletePerformanceById(Long id)
    {
        Performance performance = performanceRepository.findPerformanceByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Performance not found with id " + id));

        performance.setDeleted(true);

        performanceRepository.save(performance);
    }



    public Performance findPerformanceByMatchAndPlayer(Match match, Player player)
    {
        return performanceRepository.findPerformanceByMatchIdAndPlayerIdAndDeletedFalseAndPlayerDeletedFalseAndMatchDeletedFalse(match.getId(), player.getId())
                .orElseThrow(() -> new EntityNotFoundException("No performance found in match " + player + " for player " + match));
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
