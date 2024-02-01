package com.towsif.PlayerManagementSystem.controller;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
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
@RequestMapping("/players")
public class PlayerController
{
    private final PlayerService playerService;

    private final TeamService teamService;

    public PlayerController(PlayerService playerService, TeamService teamService)
    {
        this.playerService = playerService;
        this.teamService = teamService;
    }

    @GetMapping("/create")
    public String showCreatePlayer(Model model)
    {
        Player player = new Player();
        List<Team> teams = teamService.findAll();

        model.addAttribute("player", player);
        model.addAttribute("teams", teams);

        return "save_player";
    }

    @PostMapping("/save")
    public String savePlayer(@Valid @ModelAttribute Player player, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return "save_player";
        }

        playerService.savePlayer(player);

        return "redirect:/players/" + player.getId();
    }

    @GetMapping
    public String showAllPlayers(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortOrder,
                                 Model model)
    {
        Page<Player> playerPage = playerService.findAllPlayers(page, size, sortBy, sortOrder);

        model.addAttribute("playerPage", playerPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "players";
    }

    @GetMapping("/{id}")
    public String showPlayerById(@PathVariable Long id, Model model)
    {
        Player player = playerService.findPlayerById(id);

        model.addAttribute("player", player);

        return "player";
    }

    @GetMapping("/{id}/matches")
    public String showMatchesByPlayerId(@PathVariable Long id,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String sortBy,
                                        @RequestParam(defaultValue = "asc") String sortOrder,
                                        Model model)
    {
        Page<Match> matchPage = playerService.findMatchesByPlayerId(id, page, size, sortBy, sortOrder);

        model.addAttribute("matchPage", matchPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "matches";
    }

    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model)
    {
        Player player = playerService.findPlayerById(id);
        List<Team> teams = teamService.findAll();

        model.addAttribute("player", player);
        model.addAttribute("update", true);
        model.addAttribute("teams", teams);

        return "save_player";
    }

    @GetMapping("/{id}/delete")
    public String deletePlayer(@PathVariable Long id, Model model)
    {
        playerService.deletePlayerById(id);

        return "redirect:/players";
    }
}