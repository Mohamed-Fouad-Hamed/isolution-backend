package com.alf.accounts_service.services;

import com.alf.accounts_service.dtos.fiscalperiod.FiscalPeriodRequestDTO;
import com.alf.accounts_service.dtos.fiscalperiod.FiscalPeriodResponseDTO;
import com.alf.accounts_service.mappers.FiscalPeriodMapper;
import com.alf.accounts_service.models.core.FiscalPeriod;
import com.alf.accounts_service.models.core.FiscalYear;
import com.alf.accounts_service.repositories.FiscalPeriodRepository;
import com.alf.accounts_service.repositories.FiscalYearRepository;
import com.alf.security.common.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok generates constructor with final fields
@Transactional
public class FiscalPeriodService {
    private final FiscalPeriodRepository fiscalPeriodRepository;
    private final FiscalYearRepository fiscalYearRepository;
    private final FiscalPeriodMapper fiscalPeriodMapper;
    private final IdGeneratorService idGeneratorService ;
    private final AuditorAware<String> auditorAware;


    @Transactional
    public FiscalPeriodResponseDTO createFiscalPeriod(FiscalPeriodRequestDTO dto) throws ValidationException {
        FiscalYear fiscalYear = fiscalYearRepository.findById(dto.getFiscalYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Year not found with id: " + dto.getFiscalYearId()));

        if (fiscalYear.isClosed()) {
            throw new ValidationException("Cannot add period to a closed fiscal year.");
        }
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new ValidationException("Period end date must be after start date.");
        }
        if (dto.getStartDate().isBefore(fiscalYear.getStartDate()) || dto.getEndDate().isAfter(fiscalYear.getEndDate())) {
            throw new ValidationException("Period dates must be within the fiscal year's date range.");
        }
        fiscalPeriodRepository.findByNameAndFiscalYearId(dto.getName(), dto.getFiscalYearId())
                .ifPresent(fp -> {
                    try {
                        throw new ValidationException("Fiscal period with this name already exists for the fiscal year.");
                    } catch (ValidationException e) {
                        throw new RuntimeException(e);
                    }
                });


        FiscalPeriod fiscalPeriod = fiscalPeriodMapper.toEntity(dto);
        String serialId = idGeneratorService.generateNextId("fiscal_period");

        fiscalPeriod.setSerialId(serialId);
        fiscalPeriod.setClosed(false); // Default
        fiscalPeriod.setFiscalYear(fiscalYear);
        fiscalPeriod = fiscalPeriodRepository.save(fiscalPeriod);
        return fiscalPeriodMapper.toResponseDTO(fiscalPeriod);
    }

    @Transactional(readOnly = true)
    public FiscalPeriodResponseDTO getFiscalPeriodById(Integer id) {
        return fiscalPeriodRepository.findById(id)
                .map(fiscalPeriodMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Period not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FiscalPeriodResponseDTO> getAllFiscalPeriodsByFiscalYearId(Integer fiscalYearId) {
        if (!fiscalYearRepository.existsById(fiscalYearId)) {
            throw new ResourceNotFoundException("Fiscal Year not found with id: " + fiscalYearId);
        }
        return fiscalPeriodRepository.findByFiscalYearIdOrderByStartDateAsc(fiscalYearId).stream()
                .map(fiscalPeriodMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FiscalPeriodResponseDTO updateFiscalPeriod(Integer id, FiscalPeriodRequestDTO dto) throws ValidationException {
        FiscalPeriod existingFiscalPeriod = fiscalPeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Period not found with id: " + id));

        FiscalYear fiscalYear = fiscalYearRepository.findById(dto.getFiscalYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Year not found with id: " + dto.getFiscalYearId()));

        // Cannot change fiscal year if period is closed or year is closed
        if(existingFiscalPeriod.isClosed() && !existingFiscalPeriod.getFiscalYear().getId().equals(dto.getFiscalYearId())) {
            throw new ValidationException("Cannot change fiscal year of a closed period.");
        }
        if(fiscalYear.isClosed() && !existingFiscalPeriod.getFiscalYear().getId().equals(dto.getFiscalYearId())) {
            throw new ValidationException("Cannot move period to a closed fiscal year.");
        }


        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new ValidationException("Period end date must be after start date.");
        }
        if (dto.getStartDate().isBefore(fiscalYear.getStartDate()) || dto.getEndDate().isAfter(fiscalYear.getEndDate())) {
            throw new ValidationException("Period dates must be within the fiscal year's date range.");
        }

        // Check for name uniqueness if name or fiscalYearId is changed
        if (!existingFiscalPeriod.getName().equals(dto.getName()) || !existingFiscalPeriod.getFiscalYear().getId().equals(dto.getFiscalYearId())) {
            if (fiscalPeriodRepository.existsByFiscalYearIdAndIdNotAndName(dto.getFiscalYearId(), id, dto.getName())) {
                throw new ValidationException("Another fiscal period with this name already exists for the selected fiscal year.");
            }
        }

        fiscalPeriodMapper.updateEntityFromDto(dto, existingFiscalPeriod);
        existingFiscalPeriod.setFiscalYear(fiscalYear);

        return fiscalPeriodMapper.toResponseDTO(fiscalPeriodRepository.save(existingFiscalPeriod));
    }

    @Transactional
    public FiscalPeriodResponseDTO closeFiscalPeriod(Integer id) {
        FiscalPeriod fiscalPeriod = fiscalPeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Period not found with id: " + id));
        fiscalPeriod.setClosed(true);
        return fiscalPeriodMapper.toResponseDTO(fiscalPeriodRepository.save(fiscalPeriod));
    }

    @Transactional
    public FiscalPeriodResponseDTO openFiscalPeriod(Integer id) throws ValidationException {
        FiscalPeriod fiscalPeriod = fiscalPeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Period not found with id: " + id));
        if(fiscalPeriod.getFiscalYear().isClosed()){
            throw new ValidationException("Cannot open period in a closed fiscal year. Open the fiscal year first.");
        }
        fiscalPeriod.setClosed(false);
        return fiscalPeriodMapper.toResponseDTO(fiscalPeriodRepository.save(fiscalPeriod));
    }

    @Transactional
    public void deleteFiscalPeriod(Integer id) {
        FiscalPeriod fiscalPeriod = fiscalPeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Period not found with id: " + id));
        fiscalPeriod.setDeleted(true);
        fiscalPeriod.setDeletedDate(LocalDateTime.now());
        fiscalPeriod.setDeletedBy(auditorAware.getCurrentAuditor().orElse("system"));
        fiscalPeriodRepository.save(fiscalPeriod);
    }


}
