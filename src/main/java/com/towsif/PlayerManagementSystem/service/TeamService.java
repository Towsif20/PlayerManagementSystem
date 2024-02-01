package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.dto.TeamWithPlayerCountDTO;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import com.towsif.PlayerManagementSystem.repository.SponsorRepository;
import com.towsif.PlayerManagementSystem.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService
{
    private final TeamRepository teamRepository;

    private final PlayerRepository playerRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository, MatchRepository matchRepository, SponsorRepository sponsorRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.paginationAndSortingService = paginationAndSortingService;
    }

    public void saveTeam(Team team)
    {
        teamRepository.save(team);
    }

    public Team findTeamById(Long id)
    {
        return teamRepository.findTeamByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Team Found with id " + id));
    }

    @Transactional
    public void deleteTeamById(Long id)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Team Found with id " + id));

        team.setDeleted(true);

        teamRepository.save(team);
    }

    public List<Player> findPlayers(Long id)
    {
        return playerRepository.findPlayerByTeamIdAndDeletedFalseAndTeamDeletedFalse(id);
    }

    public List<Team> findAll()
    {
        return teamRepository.findTeamByDeletedFalse();
    }

    public Page<TeamWithPlayerCountDTO> findAllTeamsWithPlayerCount(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        Page<Object[]> resultPage = teamRepository.findAllTeamsWithPlayerCount(pageable);

        List<TeamWithPlayerCountDTO> teamWithPlayerCountDTOList = resultPage.getContent().stream()
                .map(objects -> new TeamWithPlayerCountDTO((Team) objects[0], (Long) objects[1]))
                .collect(Collectors.toList());

        return new PageImpl<>(teamWithPlayerCountDTOList, pageable, resultPage.getTotalElements());
    }
}
