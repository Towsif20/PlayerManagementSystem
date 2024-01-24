package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Performance;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

        if(!match.getPlayers().contains(player))
            throw new EntityNotFoundException("Player is not involved with this match");

        Performance performanceFromDB = performanceRepository
                .findPerformanceByMatchIdAndPlayerIdAndDeletedFalseAndPlayerDeletedFalseAndMatchDeletedFalse(matchId, playerId)
                .orElse(performance);

        performance.setId(performanceFromDB.getId());

        performance.setMatch(match);
        performance.setPlayer(player);

        return performanceRepository.save(performance);
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
        performance.setDeletedAt(LocalDateTime.now());

        performanceRepository.save(performance);
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
        return performanceRepository.findPerformanceByMatchIdAndPlayerIdAndDeletedFalseAndPlayerDeletedFalseAndMatchDeletedFalse(matchId, playerId)
                .orElseThrow(() -> new EntityNotFoundException("No performance found in match " + matchId + " for player " + playerId));
    }

    public Page<Performance> findPerformanceByMatchId(Long id, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByMatchIdAndDeletedFalseAndMatchDeletedFalse(id, pageable);
    }

    public Page<Performance> findPerformanceByPlayerId(Long id, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByPlayerIdAndDeletedFalseAndPlayerDeletedFalse(id, pageable);
    }

    public Page<Performance> findPerformanceByTeamId(Long teamId, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByPlayerTeamIdAndDeletedFalse(teamId, pageable);
    }
}
