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

import java.util.List;
import java.util.Objects;

@Service
public class MatchService
{
    private final MatchRepository matchRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository, PlayerRepository playerRepository, PerformanceRepository performanceRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.matchRepository = matchRepository;
        this.paginationAndSortingService = paginationAndSortingService;
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
    public void deleteMatch(Match match)
    {
        match.setDeleted(true);

        matchRepository.save(match);
    }


    @GetMapping("/{id}")
    public List<Player> findHomeTeamPlayers(Match match)
    {
        return match.getPlayers().stream()
                .filter(player -> Objects.equals(player.getTeam().getId(), match.getHomeTeam().getId()) && !player.isDeleted())
                .distinct()
                .toList();
    }

    @GetMapping("/{id}")
    public List<Player> findAwayTeamPlayers(Match match)
    {
        return match.getPlayers().stream()
                .filter(player -> Objects.equals(player.getTeam().getId(), match.getAwayTeam().getId()) && !player.isDeleted())
                .distinct()
                .toList();
    }

    public Page<Match> findAllMatchesByTeam(Team team, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return matchRepository.findMatchByHomeTeamIdOrAwayTeamIdAndDeletedFalseAndHomeTeamDeletedFalseAndAwayTeamDeletedFalse(team.getId(), team.getId(), pageable);
    }
}
