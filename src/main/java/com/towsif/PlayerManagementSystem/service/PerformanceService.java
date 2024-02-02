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
    public void savePerformance(Performance performance, Long matchId, Long playerId)
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



    public Performance findPerformanceByMatchIdAndPlayerId(Long matchId, Long playerId)
    {
        return performanceRepository.findPerformanceByMatchIdAndPlayerIdAndDeletedFalseAndPlayerDeletedFalseAndMatchDeletedFalse(matchId, playerId)
                .orElseThrow(() -> new EntityNotFoundException("No performance found in match " + matchId + " for player " + playerId));
    }

    public Page<Performance> findPerformanceByMatch(Match match, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByMatchIdAndDeletedFalseAndMatchDeletedFalse(match.getId(), pageable);
    }

    public Page<Performance> findPerformanceByPlayer(Player player, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByPlayerIdAndDeletedFalseAndPlayerDeletedFalse(player.getId(), pageable);
    }

    public Page<Performance> findPerformanceByTeamId(Team team, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return performanceRepository.findPerformanceByPlayerTeamIdAndDeletedFalse(team.getId(), pageable);
    }
}
