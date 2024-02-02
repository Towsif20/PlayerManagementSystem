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

    @ModelAttribute("teams")
    public List<Team> addTeamsToModel()
    {
        return teamService.findAll();
    }

    @ModelAttribute("player")
    public Player addPlayerToModel(@PathVariable(required = false) Long id)
    {
        if(id == null)
            return new Player();

        return playerService.findPlayerById(id);
    }

    @GetMapping("/create")
    public String showCreatePlayer(Model model)
    {
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
        return "player";
    }

    @GetMapping("/{id}/matches")
    public String showMatchesByPlayerId(@PathVariable Long id,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id") String sortBy,
                                        @RequestParam(defaultValue = "asc") String sortOrder,
                                        @ModelAttribute Player player,
                                        Model model)
    {
        Page<Match> matchPage = playerService.findMatchesByPlayer(player, page, size, sortBy, sortOrder);

        model.addAttribute("matchPage", matchPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "matches";
    }

    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model)
    {
        model.addAttribute("update", true);

        return "save_player";
    }

    @GetMapping("/{id}/delete")
    public String deletePlayer(@PathVariable Long id,
                               @ModelAttribute Player player,
                               Model model)
    {
        playerService.deletePlayer(player);

        return "redirect:/players";
    }
}
