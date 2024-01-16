package com.towsif.PlayerManagementSystem.controller.api;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Sponsor;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.service.TeamService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController("TeamRestController")
@RequestMapping("/api/teams/")
public class TeamController
{
    @Autowired
    private TeamService teamService;

    @PostMapping
    public Team saveTeam(@RequestBody @Valid Team team, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return teamService.saveTeam(team);
    }

    @GetMapping
    public List<Team> findAllTeams(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy,
                                   @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return teamService.findAllTeams(page, size, sortBy, sortOrder).getContent();
    }

    @GetMapping("/{id}")
    public Team findTeamById(@PathVariable("id") Long id)
    {
        return teamService.findTeamById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteTeamById(@PathVariable("id") Long id)
    {
        return teamService.deleteTeamById(id);
    }

    @PutMapping("/{id}")
    public Team updateTeamById(@PathVariable("id") Long id, @RequestBody @Valid Team team, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return teamService.updateTeamById(id, team);
    }

    @PostMapping("{teamId}/players/{playerId}/add")
    public String addPlayerToTeam(@PathVariable("teamId") Long teamId, @PathVariable("playerId") Long playerId)
    {
        return teamService.addPlayerToTeam(teamId, playerId);
    }

    @PostMapping("{teamId}/players/add")
    public String addPlayersToTeam(@PathVariable("teamId") Long teamId,
                                   @RequestBody Map<String, List<Integer>> playerIdsMap)
    {
        return teamService.addPlayersToTeam(teamId, playerIdsMap);
    }

    @PostMapping("{teamId}/players/{playerId}/remove")
    public String removePlayerFromTeam(@PathVariable("teamId") Long teamId, @PathVariable("playerId") Long playerId)
    {
        return teamService.removePlayerFromTeam(teamId, playerId);
    }

    @PostMapping("/{teamId}/sponsors/{sponsorId}/add")
    public String addSponsorToPlayer(@PathVariable("teamId") Long teamId, @PathVariable("sponsorId") Long sponsorId)
    {
        return teamService.addSponsorToTeam(teamId, sponsorId);
    }

    @GetMapping("{teamId}/players")
    public List<Player> findPlayers(@PathVariable("teamId") Long teamId)
    {
        return teamService.findPlayers(teamId);
    }

    @GetMapping("/{teamId}/matches")
    public List<Match> findMatchesByTeamId(@PathVariable("teamId") Long teamId)
    {
        return teamService.findMatchesByTeamId(teamId);
    }

    @GetMapping("/{teamId}/matches/home")
    public List<Match> findMatchesByHomeTeamId(@PathVariable("teamId") Long teamId)
    {
        return teamService.findMatchesByHomeTeamId(teamId);
    }

    @GetMapping("/{teamId}/matches/away")
    public List<Match> findMatchesByAwayTeamId(@PathVariable("teamId") Long teamId)
    {
        return teamService.findMatchesByAwayTeamId(teamId);
    }

    @GetMapping("/{teamId}/sponsors")
    public List<Sponsor> findSponsorByPlayerId(@PathVariable("teamId") Long teamId)
    {
        return teamService.findSponsorByTeamId(teamId);
    }
}
