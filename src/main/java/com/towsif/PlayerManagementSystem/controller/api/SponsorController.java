package com.towsif.PlayerManagementSystem.controller.api;

import com.towsif.PlayerManagementSystem.entity.Sponsor;
import com.towsif.PlayerManagementSystem.service.SponsorService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors/")
public class SponsorController
{
    @Autowired
    private SponsorService sponsorService;

    @PostMapping
    public Sponsor saveSponsor(@RequestBody @Valid Sponsor sponsor, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return sponsorService.saveSponsor(sponsor);
    }

    @GetMapping
    public List<Sponsor> findAllSponsors(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(defaultValue = "id") String sortBy,
                                         @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return sponsorService.findAllSponsors(page, size, sortBy, sortOrder);
    }

    @GetMapping("/{id}")
    public Sponsor findSponsorById(@PathVariable("id") Long id)
    {
        return sponsorService.findSPoSponsorById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteSponsorById(@PathVariable("id") Long id)
    {
        return sponsorService.deleteSponsorById(id);
    }

    @PutMapping("/{id}")
    public Sponsor updateSponsorById(@PathVariable("id") Long id, @RequestBody @Valid Sponsor sponsor, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return sponsorService.updateSponsorById(id, sponsor);
    }
}
