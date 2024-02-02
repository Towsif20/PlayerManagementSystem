package com.towsif.PlayerManagementSystem.controller;

import com.towsif.PlayerManagementSystem.dto.TeamWithPlayerCountDTO;
import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.service.MatchService;
import com.towsif.PlayerManagementSystem.service.PlayerService;
import com.towsif.PlayerManagementSystem.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teams")
public class TeamController
{
    private final TeamService teamService;

    private final PlayerService playerService;

    private final MatchService matchService;

    public TeamController(TeamService teamService, PlayerService playerService, MatchService matchService)
    {
        this.teamService = teamService;
        this.playerService = playerService;
        this.matchService = matchService;
    }

    @ModelAttribute("team")
    public Team addPlayerToModel(@PathVariable(required = false) Long id)
    {
        if(id == null)
            return new Team();

        return teamService.findTeamById(id);
    }

    @PostMapping("/save")
    public String saveTeam(@Valid @ModelAttribute Team team, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return "save_team";
        }

        teamService.saveTeam(team);

        return "redirect:/teams/" + team.getId();
    }

    @GetMapping("/create")
    public String showCreateTeam(Model model)
    {
        return "save_team";
    }

    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model)
    {
        model.addAttribute("update", true);

        return "save_team";
    }

    @GetMapping
    public String showAllTeams(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size,
                               @RequestParam(defaultValue = "team") String sortBy,
                               @RequestParam(defaultValue = "asc") String sortOrder,
                               Model model)
    {
        Page<TeamWithPlayerCountDTO> teamPage = teamService.findAllTeamsWithPlayerCount(page, size, sortBy, sortOrder);

        model.addAttribute("teamPage", teamPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "teams";
    }

    @GetMapping("/{id}/players")
    public String showPlayers(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortOrder,
                              @ModelAttribute Team team,
                              Model model)
    {
        Page<Player> playerPage = playerService.findAllPlayersByTeam(team, page, size, sortBy, sortOrder);

        model.addAttribute("playerPage", playerPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "players";
    }

    @GetMapping("/{id}/playersResponseBody")
    public @ResponseBody List<Player> getPlayers(@PathVariable Long id, Model model)
    {
        return teamService.findPlayers(id);
    }

    @GetMapping("{id}/matches")
    public String showMatches(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "date") String sortBy,
                              @RequestParam(defaultValue = "desc") String sortOrder,
                              @ModelAttribute Team team,
                              Model model)
    {
        Page<Match> matchPage = matchService.findAllMatchesByTeam(team, page, size, sortBy, sortOrder);

        model.addAttribute("matchPage", matchPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "matches";
    }

    @GetMapping("/{id}")
    public String showTeamById(@PathVariable Long id, Model model)
    {
        return "team";
    }

    @GetMapping("/{id}/delete")
    public String deleteTeam(@PathVariable Long id,
                             @ModelAttribute Team team,
                             Model model)
    {
        teamService.deleteTeam(team);

        return "redirect:/teams";
    }
}
