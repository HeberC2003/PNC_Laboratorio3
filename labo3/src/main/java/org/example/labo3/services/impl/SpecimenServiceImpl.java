package org.example.labo3.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.labo3.common.mappers.SpecimenMapper;
import org.example.labo3.domain.dto.request.CreateSpecimenRequest;
import org.example.labo3.domain.dto.request.UpdateSpecimenRequest;
import org.example.labo3.domain.dto.response.SpecimenResponse;
import org.example.labo3.domain.entities.Specimen;
import org.example.labo3.exceptions.ResourceNotFoundException;
import org.example.labo3.repositories.SpecimenRepository;
import org.example.labo3.services.SpecimenService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecimenServiceImpl implements SpecimenService {

    private final SpecimenRepository specimenRepository;
    private final SpecimenMapper specimenMapper;

    @Override
    public SpecimenResponse createSpecimen(CreateSpecimenRequest request) {
        return specimenMapper.toDto(
                specimenRepository.save(
                        specimenMapper.toEntityCreate(request)
                )
        );
    }

    @Override
    public Page<SpecimenResponse> getAllSpecimens(
            int page, int size, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return specimenMapper.toDtoPage(
                specimenRepository.findAll(pageable)
        );
    }

    @Override
    public SpecimenResponse getSpecimenById(UUID id) {
        Specimen specimen = specimenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Specimen not found in Sheikah Slate records"));
        return specimenMapper.toDto(specimen);
    }

    @Override
    public SpecimenResponse updateSpecimen(UUID id, UpdateSpecimenRequest request) {
        specimenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Specimen not found in Sheikah Slate records"));
        return specimenMapper.toDto(
                specimenRepository.save(
                        specimenMapper.toEntityUpdate(request, id)
                )
        );
    }

    @Override
    public SpecimenResponse deleteSpecimen(UUID id) {
        Specimen specimen = specimenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Specimen not found in Sheikah Slate records"));
        specimenRepository.deleteById(id);
        return specimenMapper.toDto(specimen);
    }
}