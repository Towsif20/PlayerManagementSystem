package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Performance;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PerformanceService
{
    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    PaginationAndSortingService paginationAndSortingService;

    @Transactional
    public Performance savePerformance(Performance performance, Long matchId, Long playerId)
    {
        Match match = matchRepository.findMatchByIdAndDeletedFalse(matchId)
                        .orElseThrow(() -> new EntityNotFoundException("No match found with id " + matchId));

        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No player found with id " + playerId));

        Performance performanceFromDB = performanceRepository
                .findPerformanceByMatchIdAndPlayerIdAndDeletedFalse(matchId, playerId)
                .orElse(performance);

        performance.setId(performanceFromDB.getId());

        performance.setCreatedAt(LocalDateTime.now());

        performance.setMatch(match);
        performance.setPlayer(player);

        return performanceRepository.save(performance);
    }

    public List<Performance> findAllPerformances(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByDeletedFalse(pageable).getContent();
    }

    public Performance findPerformanceById(Long id)
    {
        return performanceRepository.findPerformanceByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Performance not found with id " + id));
    }

    @Transactional
    public String deletePerformanceById(Long id)
    {
        Performance performance = performanceRepository.findPerformanceByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Performance not found with id " + id));

        performance.setDeleted(true);
        performance.setDeletedAt(LocalDateTime.now());

        performanceRepository.save(performance);

        return "Deleted";
    }

    @Transactional
    public Performance updatePerformanceById(Long id, Performance performanceFromRequest)
    {
        Performance performanceFromDB = performanceRepository.findPerformanceByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Performance not found with id " + id));

        performanceFromRequest.setId(id);
        performanceFromRequest.setUpdatedAt(LocalDateTime.now());
        performanceFromRequest.setCreatedAt(performanceFromDB.getCreatedAt());

        return performanceRepository.save(performanceFromRequest);
    }

    public Performance findPerformanceByMatchIdAndPlayerId(Long matchId, Long playerId)
    {
        return performanceRepository.findPerformanceByMatchIdAndPlayerIdAndDeletedFalse(matchId, playerId)
                .orElseThrow(() -> new EntityNotFoundException("No performance found in match " + matchId + " for player " + playerId));
    }

    public List<Performance> findPerformanceByMatchId(Long id, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByMatchIdAndDeletedFalse(id, pageable).getContent();
    }

    public List<Performance> findPerformanceByPlayerId(Long id, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByPlayerIdAndDeletedFalse(id, pageable).getContent();
    }
}
