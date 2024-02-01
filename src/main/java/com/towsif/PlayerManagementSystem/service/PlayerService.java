package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import com.towsif.PlayerManagementSystem.repository.SponsorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerService
{
    private final PlayerRepository playerRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public PlayerService(PlayerRepository playerRepository, MatchRepository matchRepository, SponsorRepository sponsorRepository, PerformanceRepository performanceRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.playerRepository = playerRepository;
        this.paginationAndSortingService = paginationAndSortingService;
    }

    public void savePlayer(Player player)
    {
        playerRepository.save(player);
    }

    public Page<Player> findAllPlayers(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return playerRepository.findPlayerByDeletedFalse(pageable);
    }

    public Page<Player> findAllPlayersByTeam(int page, int size, String sortBy, String sortOrder, Long teamId)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return playerRepository.findPlayerByTeamIdAndDeletedFalseAndTeamDeletedFalse(pageable, teamId);
    }

    public Player findPlayerById(Long id)
    {
        return playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));
    }

    @Transactional
    public void deletePlayerById(Long id)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        player.setDeleted(true);

        playerRepository.save(player);
    }


    public Page<Match> findMatchesByPlayerId(Long id, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return playerRepository.findMatchesByPlayerId(id, pageable);
    }

}
