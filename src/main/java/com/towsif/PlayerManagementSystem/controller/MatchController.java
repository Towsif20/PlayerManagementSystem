package com.towsif.PlayerManagementSystem.controller;

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
@RequestMapping("/matches")
public class MatchController
{
    private final MatchService matchService;

    private final TeamService teamService;
    private final PlayerService playerService;

    public MatchController(MatchService matchService, TeamService teamService, PlayerService playerService)
    {
        this.matchService = matchService;
        this.teamService = teamService;
        this.playerService = playerService;
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

    @ModelAttribute("matchPage")
    public Page<Match> addMatchPageToModel(@PathVariable(required = false) Long playerId,
                                           @PathVariable(required = false) Long teamId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "date") String sortBy,
                                           @RequestParam(defaultValue = "desc") String sortOrder)
    {
        if(playerId != null)
            return playerService.findMatchesByPlayer(playerId, page, size, sortBy, sortOrder);

        if(teamId != null)
            return matchService.findAllMatchesByTeam(teamId, page, size, sortBy, sortOrder);

        return matchService.findAllMatches(page, size, sortBy, sortOrder);
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
        List<Player> availableHomePlayers = teamService.findPlayers(match.getHomeTeam());
        List<Player> availableAwayPlayers = teamService.findPlayers(match.getAwayTeam());

        model.addAttribute("availableHomePlayers", availableHomePlayers);
        model.addAttribute("availableAwayPlayers", availableAwayPlayers);
        model.addAttribute("update", true);

        return "save_match";
    }

    @PostMapping("/save")
    public String saveMatch(@Valid @ModelAttribute Match match, BindingResult bindingResult)
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
        return "matches";
    }

    @GetMapping("/teams/{teamId}")
    public String showAllMatchesByTeam(@PathVariable Long teamId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "date") String sortBy,
                                       @RequestParam(defaultValue = "desc") String sortOrder,
                                       Model model)
    {
        return "matches";
    }

    @GetMapping("/players/{playerId}")
    public String showAllMatchesByPlayer(@PathVariable Long playerId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "date") String sortBy,
                                         @RequestParam(defaultValue = "desc") String sortOrder,
                                         Model model)
    {
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
