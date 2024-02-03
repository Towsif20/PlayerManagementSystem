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

@Controller
@RequestMapping("/teams")
public class TeamController
{
    private final TeamService teamService;

    public TeamController(TeamService teamService)
    {
        this.teamService = teamService;
    }

    @ModelAttribute("team")
    public Team addPlayerToModel(@PathVariable(required = false) Long id)
    {
        if(id == null)
            return new Team();

        return teamService.findTeamById(id);
    }

    @ModelAttribute("teamPage")
    public Page<TeamWithPlayerCountDTO> addTeamPageToModel(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "team.name") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return teamService.findAllTeamsWithPlayerCount(page, size, sortBy, sortOrder);
    }

    @ModelAttribute("sortBy")
    public String addSortParameterToModel(@RequestParam(defaultValue = "id") String sortBy)
    {
        return sortBy;
    }

    @ModelAttribute("sortOrder")
    public String addSortOrderToModel(@RequestParam(defaultValue = "asc") String sortOrder)
    {
        return sortOrder;
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
        return "teams";
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
