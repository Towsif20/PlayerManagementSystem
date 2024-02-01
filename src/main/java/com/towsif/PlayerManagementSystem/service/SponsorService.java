package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Sponsor;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.SponsorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SponsorService
{
    private final SponsorRepository sponsorRepository;

    private final PaginationAndSortingService paginationAndSortingService;

    public SponsorService(SponsorRepository sponsorRepository, PaginationAndSortingService paginationAndSortingService)
    {
        this.sponsorRepository = sponsorRepository;
        this.paginationAndSortingService = paginationAndSortingService;
    }

    public void saveSponsor(Sponsor sponsor)
    {
        sponsorRepository.save(sponsor);
    }

    public Page<Sponsor> findAllSponsors(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return sponsorRepository.findSponsorByDeletedFalse(pageable);
    }

    public Sponsor findSPoSponsorById(Long id)
    {
        return sponsorRepository.findSponsorByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No sponsor found with id " + id));
    }

    @Transactional
    public void deleteSponsorById(Long id)
    {
        Sponsor sponsor = sponsorRepository.findSponsorByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No sponsor found with id " + id));

        sponsor.setDeleted(true);

        sponsorRepository.save(sponsor);
    }

    public List<Team> findTeamsBySponsorId(Long sponsorId)
    {
        return sponsorRepository.findTeamsBySponsorId(sponsorId);
    }
}
