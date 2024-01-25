package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import com.towsif.PlayerManagementSystem.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MatchService
{
    private final MatchRepository matchRepository;

    private final TeamRepository teamRepository;

    private final PlayerRepository playerRepository;

    private final PerformanceRepository performanceRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository, PlayerRepository playerRepository, PerformanceRepository performanceRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.performanceRepository = performanceRepository;
        this.paginationAndSortingService = paginationAndSortingService;
    }

    public Match saveMatch(Match match, Long homeTeamId, Long awayTeamId)
    {
        Team homeTeam = teamRepository.findTeamByIdAndDeletedFalse(homeTeamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Found with id " + homeTeamId));

        Team awayTeam = teamRepository.findTeamByIdAndDeletedFalse(awayTeamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Found with id " + awayTeamId));

        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);

        return matchRepository.save(match);
    }

    public void saveMatch(Match match)
    {
        matchRepository.save(match);
    }

    public Page<Match> findAllMatches(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return matchRepository.findMatchByDeletedFalse(pageable);
    }

    public Match findMatchById(Long id)
    {
        return matchRepository.findMatchByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + id));
    }

    @Transactional
    public void deleteMatchById(Long id)
    {
        Match match = matchRepository.findMatchByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + id));

        match.setDeleted(true);
        match.setDeletedAt(LocalDateTime.now());

        matchRepository.save(match);
    }

    @Transactional
    public Match updateMatchById(Long id, Match matchFromRequest)
    {
        Match matchFromDB = matchRepository.findMatchByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + id));

        matchFromRequest.setId(id);
        matchFromRequest.setUpdatedAt(LocalDateTime.now());
        matchFromRequest.setCreatedAt(matchFromDB.getCreatedAt());

        return matchRepository.save(matchFromRequest);
    }


    public List<Player> findPlayersByMatchId(Long matchId)
    {
        Match match = matchRepository.findMatchByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + matchId));

        return match.getPlayers().stream()
                .filter(player -> !player.isDeleted())
                .distinct()
                .toList();
    }

    @GetMapping("/{id}")
    public List<Player> findHomeTeamPlayers(@PathVariable Long matchId)
    {
        Match match = matchRepository.findMatchByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + matchId));

        return match.getPlayers().stream()
                .filter(player -> Objects.equals(player.getTeam().getId(), match.getHomeTeam().getId()) && !player.isDeleted())
                .distinct()
                .toList();
    }

    @GetMapping("/{id}")
    public List<Player> findAwayTeamPlayers(@PathVariable Long matchId)
    {
        Match match = matchRepository.findMatchByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + matchId));

        return match.getPlayers().stream()
                .filter(player -> Objects.equals(player.getTeam().getId(), match.getAwayTeam().getId()) && !player.isDeleted())
                .distinct()
                .toList();
    }

    public void addPlayersToMatch(Long matchId, Map<String, List<Integer>> playerIdsMap)
    {
        Match match = matchRepository.findMatchByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + matchId));

        List<Player> homePLayers = this.findHomeTeamPlayers(matchId); //playerRepository.findPlayerByTeamIdAndDeletedFalse(match.getHomeTeam().getId());
        List<Player> awayPLayers = this.findAwayTeamPlayers(matchId); //playerRepository.findPlayerByTeamIdAndDeletedFalse(match.getAwayTeam().getId());

        List<Long> playerIds = new ArrayList<>();
        playerIdsMap.get("playerIds").forEach(id -> playerIds.add(Long.valueOf(id)));

        List<Player> players = playerRepository.findAllById(playerIds);

        players = players.stream()
                        .filter(player -> homePLayers.contains(player) || awayPLayers.contains(player))
                        .toList();

        match.getPlayers().addAll(players);

        matchRepository.save(match);
    }

    public List<Team> findTeams(Long id)
    {
        Match match = matchRepository.findMatchByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + id));

        return Arrays.asList(match.getHomeTeam(), match.getAwayTeam());
    }

    public Long findRunsByMatchId(Long id)
    {
        return performanceRepository.findRunsByMatchId(id);
    }

    public Long findRunsByMatchIdAndPlayerId(Long matchId, Long teamId)
    {
        return performanceRepository.findTotalRunsInAMatchByTeamId(matchId, teamId);
    }

    public Page<Match> findAllMatchesByTeam(Long id, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return matchRepository.findMatchByHomeTeamIdOrAwayTeamIdAndDeletedFalseAndHomeTeamDeletedFalseAndAwayTeamDeletedFalse(id, id, pageable);
    }
}
