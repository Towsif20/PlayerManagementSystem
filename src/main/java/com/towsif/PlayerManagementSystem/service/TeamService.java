package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.dto.TeamWithPlayerCountDTO;
import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Sponsor;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamService
{
    private final TeamRepository teamRepository;

    private final PlayerRepository playerRepository;

    private final MatchRepository matchRepository;

    private final SponsorRepository sponsorRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public TeamService(TeamRepository teamRepository, PlayerRepository playerRepository, MatchRepository matchRepository, SponsorRepository sponsorRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
        this.sponsorRepository = sponsorRepository;
        this.paginationAndSortingService = paginationAndSortingService;
    }

    public Team saveTeam(Team team)
    {
        return teamRepository.save(team);
    }

    @Transactional
    public Page<Team> findAllTeams(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return teamRepository.findTeamByDeletedFalse(pageable);
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
        team.setDeletedAt(LocalDateTime.now());

        teamRepository.save(team);
    }

    @Transactional
    public Team updateTeamById(Long id, Team teamFromRequest)
    {
        Team teamFromDB = teamRepository.findTeamByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        teamFromRequest.setId(id);
        teamFromRequest.setUpdatedAt(LocalDateTime.now());
        teamFromRequest.setCreatedAt(teamFromDB.getCreatedAt());

        return teamRepository.save(teamFromRequest);
    }

    @Transactional
    public void addPlayerToTeam(Long teamId, Long playerId)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No team found with id " + playerId));

        player.setTeam(team);

        playerRepository.save(player);
    }

    public void removePlayerFromTeam(Long teamId, Long playerId)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No team found with id " + playerId));

        player.setTeam(null);

        playerRepository.save(player);
    }

    public List<Player> findPlayers(Long id)
    {
        return playerRepository.findPlayerByTeamIdAndDeletedFalseAndTeamDeletedFalse(id);
    }

    public void addPlayersToTeam(Long teamId, Map<String, List<Integer>> playerIdsMap)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        List<Long> playerIds = new ArrayList<>();
        playerIdsMap.get("playerIds").forEach(id -> playerIds.add(Long.valueOf(id)));

        List<Player> players = playerRepository.findAllById(playerIds);

        players.forEach(player -> player.setTeam(team));

        teamRepository.save(team);
    }

    public Page<Match> findMatchesByTeamId(Long teamId, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return matchRepository.findMatchByHomeTeamIdOrAwayTeamIdAndDeletedFalseAndHomeTeamDeletedFalseAndAwayTeamDeletedFalse(teamId, teamId, pageable);
    }

    public Page<Match> findMatchesByHomeTeamId(Long teamId, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return matchRepository.findMatchByHomeTeamIdAndDeletedFalseAndHomeTeamDeletedFalse(teamId, pageable);
    }

    public Page<Match> findMatchesByAwayTeamId(Long teamId, int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return matchRepository.findMatchByAwayTeamIdAndDeletedFalseAndAwayTeamDeletedFalse(teamId, pageable);
    }

    public void addSponsorToTeam(Long teamId, Long sponsorId)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        Sponsor sponsor = sponsorRepository.findSponsorByIdAndDeletedFalse(sponsorId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + sponsorId));

        team.getSponsors().add(sponsor);
    }

    public List<Sponsor> findSponsorByTeamId(Long teamId)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        return team.getSponsors();
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
