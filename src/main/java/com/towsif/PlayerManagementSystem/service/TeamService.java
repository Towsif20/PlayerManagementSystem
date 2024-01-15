package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Sponsor;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import com.towsif.PlayerManagementSystem.repository.SponsorRepository;
import com.towsif.PlayerManagementSystem.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TeamService
{
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private PaginationAndSortingService paginationAndSortingService;

    public Team saveTeam(Team team)
    {
        team.setCreatedAt(LocalDateTime.now());

        return teamRepository.save(team);
    }

    @Transactional
    public List<Team> findAllTeams(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return teamRepository.findTeamByDeletedFalse(pageable).getContent();
    }

    public Team findTeamById(Long id)
    {
        return teamRepository.findTeamByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Team Found with id " + id));
    }

    @Transactional
    public String deleteTeamById(Long id)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Team Found with id " + id));

        team.setDeleted(true);
        team.setDeletedAt(LocalDateTime.now());

        teamRepository.save(team);

        return "Deleted";
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
    public String addPlayerToTeam(Long teamId, Long playerId)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No team found with id " + playerId));

        player.setTeam(team);

        playerRepository.save(player);

        return "Added";
    }

    public String removePlayerFromTeam(Long teamId, Long playerId)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No team found with id " + playerId));

        player.setTeam(null);

        playerRepository.save(player);

        return "Removed";
    }

    public List<Player> findPlayers(Long id)
    {
        return playerRepository.findPlayerByTeamIdAndDeletedFalse(id);
    }

    public String addPlayersToTeam(Long teamId, Map<String, List<Integer>> playerIdsMap)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        List<Long> playerIds = new ArrayList<>();
        playerIdsMap.get("playerIds").forEach(id -> playerIds.add(Long.valueOf(id)));

        List<Player> players = playerRepository.findAllById(playerIds);

        players.forEach(player -> player.setTeam(team));

        teamRepository.save(team);

        return "Added players to team";
    }

    public List<Match> findMatchesByTeamId(Long teamId)
    {
        return matchRepository.findMatchByHomeTeamIdOrAwayTeamIdAndDeletedFalse(teamId, teamId);
    }

    public List<Match> findMatchesByHomeTeamId(Long teamId)
    {
        return matchRepository.findMatchByHomeTeamIdAndDeletedFalse(teamId);
    }

    public List<Match> findMatchesByAwayTeamId(Long teamId)
    {
        return matchRepository.findMatchByAwayTeamIdAndDeletedFalse(teamId);
    }

    public String addSponsorToTeam(Long teamId, Long sponsorId)
    {
        Team team = teamRepository.findTeamByIdAndDeletedFalse(teamId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + teamId));

        Sponsor sponsor = sponsorRepository.findSponsorByIdAndDeletedFalse(sponsorId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + sponsorId));

        team.getSponsors().add(sponsor);

        return "Added sponsor to team";
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
}
