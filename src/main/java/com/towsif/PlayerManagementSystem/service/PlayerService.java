package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Sponsor;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import com.towsif.PlayerManagementSystem.repository.SponsorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PlayerService
{
    private final PlayerRepository playerRepository;

    private final MatchRepository matchRepository;

    private final SponsorRepository sponsorRepository;

    private final PerformanceRepository performanceRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public PlayerService(PlayerRepository playerRepository, MatchRepository matchRepository, SponsorRepository sponsorRepository, PerformanceRepository performanceRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.sponsorRepository = sponsorRepository;
        this.performanceRepository = performanceRepository;
        this.paginationAndSortingService = paginationAndSortingService;
    }

    public Player savePlayer(Player player)
    {
        return playerRepository.save(player);
    }

    public void addMatchToPlayer(Long playerId, Long matchId)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + playerId));

        Match match = matchRepository.findMatchByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new EntityNotFoundException("No match Found with id " + matchId));

        match.getPlayers().add(player);

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

    @Transactional
    public Player updatePlayerById(Long id, Player playerFromRequest)
    {
        Player playerFromDB = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        playerFromRequest.setId(id);
        playerFromRequest.setUpdatedAt(LocalDateTime.now());
        playerFromRequest.setCreatedAt(playerFromDB.getCreatedAt());

        return playerRepository.save(playerFromRequest);
    }


    public Page<Match> findMatchesByPlayerId(Long id, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return playerRepository.findMatchesByPlayerId(id, pageable);
    }

    public Team findTeamById(Long id)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        return player.getTeam();
    }

    public void addSponsorToPlayer(Long playerId, Long sponsorId)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + playerId));

        Sponsor sponsor = sponsorRepository.findSponsorByIdAndDeletedFalse(sponsorId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + sponsorId));

        player.getSponsors().add(sponsor);

        playerRepository.save(player);
    }

    public List<Sponsor> findSponsorByPlayerId(Long id)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        return player.getSponsors();
    }

    public long findRunsByPlayerId(Long id)
    {
        return performanceRepository.findRunsByPlayerId(id);
    }
}
