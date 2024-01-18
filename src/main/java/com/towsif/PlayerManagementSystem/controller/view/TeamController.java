package com.towsif.PlayerManagementSystem.controller.view;

import com.towsif.PlayerManagementSystem.dto.TeamWithPlayerCountDTO;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.service.PlayerService;
import com.towsif.PlayerManagementSystem.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TeamService teamService;

    @Autowired
    private PlayerService playerService;

    @PostMapping("/save")
    public String saveTeam(@Valid @ModelAttribute Team team, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            System.out.println(bindingResult.getAllErrors());
            return "save_team";
        }

        teamService.saveTeam(team);

        return "redirect:/teams/" + team.getId();
    }

    @GetMapping("/create")
    public String showCreateTeam(Model model)
    {
        Team team = new Team();

        model.addAttribute("team", team);

        return "save_team";
    }

    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable("id") Long id, Model model)
    {
        Team team = teamService.findTeamById(id);

        model.addAttribute("team", team);
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
    public String showPlayers(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortOrder,
                              @PathVariable("id") Long id,
                              Model model)
    {
        Page<Player> playerPage = playerService.findAllPlayersByTeam(page, size, sortBy, sortOrder, id);

        model.addAttribute("playerPage", playerPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "players";
    }

    @GetMapping("/{id}")
    public String showTeamById(@PathVariable("id") Long id, Model model)
    {
        Team team = teamService.findTeamById(id);

        model.addAttribute("team", team);

        return "team";
    }

    @GetMapping("/{id}/delete")
    public String deleteTeam(@PathVariable("id") Long id, Model model)
    {
        teamService.deleteTeamById(id);

        return "redirect:/teams";
    }
}
