package com.towsif.PlayerManagementSystem.controller.api;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.service.PlayerService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players/")
public class PlayerController
{
    @Autowired
    private PlayerService playerService;

    @PostMapping
    public Player savePlayer(@Valid @RequestBody Player player, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return playerService.savePlayer(player);
    }

    @PostMapping("/{playerId}/matches/{matchId}")
    public String addMatchToPlayer(@PathVariable("playerId") Long playerId, @PathVariable("matchId")  Long matchId)
    {
        return playerService.addMatchToPlayer(playerId, matchId);
    }

    @GetMapping
    public List<Player> findAllPlayers(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String sortBy,
                                       @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return playerService.findAllPlayers(page, size, sortBy, sortOrder);
    }

    @GetMapping("/{id}")
    public Player findPlayerById(@PathVariable("id") Long id)
    {
        return playerService.findPlayerById(id);
    }

    @DeleteMapping("/{id}")
    public String deletePlayerById(@PathVariable("id") Long id)
    {
        return playerService.deletePlayerById(id);
    }

    @PutMapping("/{id}")
    public Player updatePlayerById(@PathVariable("id") Long id, @RequestBody @Valid Player player, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return playerService.updatePlayerById(id, player);
    }

    @GetMapping("/{id}/matches")
    public List<Match> findMatchesByPlayerId(@PathVariable("id") Long id)
    {
        return playerService.findMatchesByPlayerId(id);
    }

    @GetMapping("/{id}/team")
    public Team findTeamByPlayer(@PathVariable("id") Long id)
    {
        return playerService.findTeamById(id);
    }
}
