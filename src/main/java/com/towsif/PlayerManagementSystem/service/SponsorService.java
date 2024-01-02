package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Sponsor;
import com.towsif.PlayerManagementSystem.repository.SponsorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SponsorService
{
    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    PaginationAndSortingService paginationAndSortingService;

    public Sponsor saveSponsor(Sponsor sponsor)
    {
        sponsor.setCreatedAt(LocalDateTime.now());

        return sponsorRepository.save(sponsor);
    }

    public List<Sponsor> findAllSponsors(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return sponsorRepository.findSponsorByDeletedFalse(pageable).getContent();
    }

    public Sponsor findSPoSponsorById(Long id)
    {
        return sponsorRepository.findSponsorByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No sponsor found with id " + id));
    }

    @Transactional
    public String deleteSponsorById(Long id)
    {
        Sponsor sponsor = sponsorRepository.findSponsorByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No sponsor found with id " + id));

        sponsor.setDeleted(true);
        sponsor.setDeletedAt(LocalDateTime.now());

        sponsorRepository.save(sponsor);

        return "Deleted";
    }

    @Transactional
    public Sponsor updateSponsorById(Long id, Sponsor sponsorFromRequest)
    {
        Sponsor sponsorFromDB = sponsorRepository.findSponsorByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No sponsor found with id " + id));

        sponsorFromRequest.setId(id);
        sponsorFromRequest.setUpdatedAt(LocalDateTime.now());
        sponsorFromRequest.setCreatedAt(sponsorFromDB.getCreatedAt());

        return sponsorRepository.save(sponsorFromRequest);
    }
}
