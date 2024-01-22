package com.towsif.PlayerManagementSystem.controller.view;

import com.towsif.PlayerManagementSystem.entity.Performance;
import com.towsif.PlayerManagementSystem.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/performances")
public class PerformanceController
{
    @Autowired
    PerformanceService performanceService;

    @GetMapping
    public String showAllPerformances(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy,
                                      @RequestParam(defaultValue = "asc") String sortOrder,
                                      Model model)
    {
        Page<Performance> performancePage = performanceService.findAllPerformances(page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }

    @GetMapping("/players/{playerId}")
    public String showAllPerformancesByPlayer(@PathVariable Long playerId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String sortOrder,
                                              Model model)
    {
        Page<Performance> performancePage = performanceService.findPerformanceByPlayerId(playerId, page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }

    @GetMapping("/teams/{teamId}")
    public String showAllPerformancesByTeam(@PathVariable Long teamId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortOrder,
                                            Model model)
    {
        Page<Performance> performancePage = performanceService.findPerformanceByTeamId(teamId, page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }

    @GetMapping("/matches/{matchId}")
    public String showAllPerformancesByMatch(@PathVariable Long matchId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "asc") String sortOrder,
                                             Model model)
    {
        Page<Performance> performancePage = performanceService.findPerformanceByMatchId(matchId, page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }

    @GetMapping("/matches/{matchId}/players/{playerId}")
    public String showPerformanceByPlayerAndMatch(@PathVariable Long matchId,
                                                  @PathVariable Long playerId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder,
                                                  Model model)
    {
        Page<Performance> performancePage = performanceService.findPerformanceByPlayerId(playerId, page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }
}
