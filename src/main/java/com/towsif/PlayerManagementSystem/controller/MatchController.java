package com.towsif.PlayerManagementSystem.controller;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.service.MatchService;
import com.towsif.PlayerManagementSystem.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/matches")
public class MatchController
{
    private final MatchService matchService;

    private final TeamService teamService;

    public MatchController(MatchService matchService, TeamService teamService)
    {
        this.matchService = matchService;
        this.teamService = teamService;
    }

    @ModelAttribute("match")
    public Match addMatchToModel(@PathVariable(required = false) Long id)
    {
        if(id == null)
            return new Match();

        return matchService.findMatchById(id);
    }

    @ModelAttribute("teams")
    public List<Team> addTeamsToModel()
    {
        return teamService.findAll();
    }

    @GetMapping("/create")
    public String showCreateMatch(Model model)
    {
        return "save_match";
    }

    @GetMapping("/{id}/update")
    public String showUpdateMatch(@PathVariable Long id,
                                  @ModelAttribute Match match,
                                  Model model)
    {
        List<Player> homePlayers = teamService.findPlayers(match.getHomeTeam().getId());
        List<Player> awayPlayers = teamService.findPlayers(match.getAwayTeam().getId());

        model.addAttribute("availableHomePlayers", homePlayers);
        model.addAttribute("availableAwayPlayers", awayPlayers);
        model.addAttribute("update", true);

        return "save_match";
    }

    @PostMapping("/save")
    public String savePlayer(@Valid @ModelAttribute Match match, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return "save_match";
        }

        matchService.saveMatch(match);

        return "redirect:/matches/" + match.getId();
    }

    @GetMapping
    public String showAllMatches(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "date") String sortBy,
                                 @RequestParam(defaultValue = "desc") String sortOrder,
                                 Model model)
    {
        Page<Match> matchPage = matchService.findAllMatches(page, size, sortBy, sortOrder);

        model.addAttribute("matchPage", matchPage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "matches";
    }

    @GetMapping("/{id}")
    public String showMatchById(@PathVariable Long id, Model model)
    {
        return "match";
    }

    @GetMapping("/{id}/players")
    public String showPlayersByMatchId(@PathVariable Long id,
                                       @ModelAttribute Match match,
                                       Model model)
    {
        List<Player> homePlayers = matchService.findHomeTeamPlayers(match);
        List<Player> awayPlayers = matchService.findAwayTeamPlayers(match);

        model.addAttribute("match", match);
        model.addAttribute("homePlayers", homePlayers);
        model.addAttribute("awayPlayers", awayPlayers);

        return "match_players";
    }

    @GetMapping("/{id}/delete")
    public String deleteMatchById(@ModelAttribute Match match, Model model)
    {
        matchService.deleteMatch(match);

        return "redirect:/matches";
    }
}
