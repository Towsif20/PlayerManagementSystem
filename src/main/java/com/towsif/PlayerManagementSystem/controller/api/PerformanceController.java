package com.towsif.PlayerManagementSystem.controller.api;

import com.towsif.PlayerManagementSystem.entity.Performance;
import com.towsif.PlayerManagementSystem.service.PerformanceService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("PerformanceRestController")
@RequestMapping("/api/performances/")
public class PerformanceController
{
    @Autowired
    private PerformanceService performanceService;

    @PostMapping("/matches/{matchId}/players/{playerId}")
    public Performance savePerformance(@RequestBody @Valid Performance performance, BindingResult bindingResult,
                                       @PathVariable("matchId") Long matchId,
                                       @PathVariable("playerId") Long playerId)
    {
        if(bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return performanceService.savePerformance(performance, matchId, playerId);
    }

    @GetMapping
    public List<Performance> findAllPerformances(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                 @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return performanceService.findAllPerformances(page, size, sortBy, sortOrder).getContent();
    }

    @GetMapping("/{id}")
    public Performance findPerformanceById(@PathVariable("id") Long id)
    {
        return performanceService.findPerformanceById(id);
    }

    @DeleteMapping("/{id}")
    public String deletePerformanceById(@PathVariable("id") Long id)
    {
        performanceService.deletePerformanceById(id);

        return "Deleted";
    }

    @PutMapping("/{id}")
    public Performance updatePerformanceById(@PathVariable("id") Long id, @RequestBody @Valid Performance performance, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return performanceService.updatePerformanceById(id, performance);
    }

    @GetMapping("/matches/{matchId}/players/{playerId}")
    public Performance findPerformanceByMatchIdAndPlayerId(@PathVariable("matchId") Long matchId,
                                                           @PathVariable("playerId") Long playerId)
    {
        return performanceService.findPerformanceByMatchIdAndPlayerId(matchId, playerId);
    }

    @GetMapping("/matches/{matchId}")
    public List<Performance> findPerformanceByMatchId(@PathVariable("matchId") Long id,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "id") String sortBy,
                                                      @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return performanceService.findPerformanceByMatchId(id, page, size, sortBy, sortOrder).getContent();
    }

    @GetMapping("/players/{playerId}")
    public List<Performance> findPerformanceByPlayerId(@PathVariable("playerId") Long id,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return performanceService.findPerformanceByPlayerId(id, page, size, sortBy, sortOrder).getContent();
    }
}
