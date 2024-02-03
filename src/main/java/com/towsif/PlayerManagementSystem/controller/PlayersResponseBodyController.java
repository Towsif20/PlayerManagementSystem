package com.towsif.PlayerManagementSystem.controller;

import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/playersResponseBody")
public class PlayersResponseBodyController
{
    private final PlayerRepository playerRepository;

    public PlayersResponseBodyController(PlayerRepository playerRepository)
    {
        this.playerRepository = playerRepository;
    }

    @GetMapping("/teams/{id}")
    public @ResponseBody List<Player> getPlayers(@PathVariable Long id)
    {
        return playerRepository.findPlayerByTeamIdAndDeletedFalseAndTeamDeletedFalse(id);
    }
}
