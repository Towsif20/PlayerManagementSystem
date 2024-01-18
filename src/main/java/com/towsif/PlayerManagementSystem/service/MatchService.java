package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import com.towsif.PlayerManagementSystem.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class MatchService
{
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    PerformanceRepository performanceRepository;

    @Autowired
    private PaginationAndSortingService paginationAndSortingService;


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

    public List<Match> findAllMatches(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return matchRepository.findMatchByDeletedFalse(pageable).getContent();
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
        return matchRepository.findPlayersByMatchId(matchId);
    }

    public void addPlayersToMatch(Long matchId, Map<String, List<Integer>> playerIdsMap)
    {
        Match match = matchRepository.findMatchByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new EntityNotFoundException("No match found with " + matchId));

        List<Player> homePLayers = playerRepository.findPlayerByTeamIdAndDeletedFalse(match.getHomeTeam().getId());
        List<Player> awayPLayers = playerRepository.findPlayerByTeamIdAndDeletedFalse(match.getAwayTeam().getId());

        List<Long> playerIds = new ArrayList<>();
        playerIdsMap.get("playerIds").forEach(id -> playerIds.add(Long.valueOf(id)));

        List<Player> players = playerRepository.findAllById(playerIds);

        players = players.stream()
                        .filter(player -> homePLayers.contains(player) || awayPLayers.contains(player))
                        .toList();

        players.forEach(player -> player.getMatches().add(match));

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
}
