package com.towsif.PlayerManagementSystem.controller.view;

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
@RequestMapping("/players")
public class PlayerController
{
    @Autowired
    private PlayerService playerService;

    @Autowired
    private TeamService teamService;

    @GetMapping("/create")
    public String showCreatePlayer(Model model)
    {
        Player player = new Player();
        List<Team> teams = teamService.findAll();

        model.addAttribute("player", player);
        model.addAttribute("teams", teams);

        return "create_player";
    }

    @PostMapping
    public String savePlayer(@Valid @ModelAttribute Player player, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            System.out.println(bindingResult.getAllErrors());
            return "create_player";
        }

        playerService.savePlayer(player);

        return "redirect:/players";
    }

    @GetMapping("")
    public String findAllPlayers(@RequestParam(defaultValue = "0") int page,
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
    public String findPlayerById(@PathVariable("id") Long id, Model model)
    {
        Player player = playerService.findPlayerById(id);

        model.addAttribute("player", player);

        return "player";
    }

    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable("id") Long id, Model model)
    {
        Player player = playerService.findPlayerById(id);
        List<Team> teams = teamService.findAll();

        model.addAttribute("player", player);
        model.addAttribute("update", true);
        model.addAttribute("teams", teams);

        return "create_player";
    }

    @GetMapping("/{id}/delete")
    public String deletePlayer(@PathVariable("id") Long id, Model model)
    {
        playerService.deletePlayerById(id);

        return "redirect:/";
    }
}
