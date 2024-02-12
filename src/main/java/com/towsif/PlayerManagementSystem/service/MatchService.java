package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class MatchService
{
    private final MatchRepository matchRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public MatchService(MatchRepository matchRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.matchRepository = matchRepository;
        this.paginationAndSortingService = paginationAndSortingService;
    }

    @Transactional
    public void saveMatch(Match match)
    {
        Match existingMatch = matchRepository.findMatchByIdAndDeletedFalse(match.getId()).orElse(null);

        if(existingMatch != null)
        {
            existingMatch.getPlayers().clear();

            matchRepository.save(existingMatch);
        }

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


    public List<Player> findHomeTeamPlayers(Match match)
    {
        return match.getPlayers().stream()
                .filter(player -> Objects.equals(player.getTeam().getId(), match.getHomeTeam().getId()) && !player.isDeleted())
                .distinct()
                .toList();
    }

    public List<Player> findAwayTeamPlayers(Match match)
    {
        return match.getPlayers().stream()
                .filter(player -> Objects.equals(player.getTeam().getId(), match.getAwayTeam().getId()) && !player.isDeleted())
                .distinct()
                .toList();
    }

    public Page<Match> findAllMatchesByTeam(Long teamId, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return matchRepository.findMatchByHomeTeamIdOrAwayTeamIdAndDeletedFalseAndHomeTeamDeletedFalseAndAwayTeamDeletedFalse(teamId, teamId, pageable);
    }
}
