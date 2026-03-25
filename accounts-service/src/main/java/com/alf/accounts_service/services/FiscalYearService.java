package com.alf.accounts_service.services;

import com.alf.accounts_service.dtos.fiscalperiod.FiscalYearRequestDTO;
import com.alf.accounts_service.dtos.fiscalperiod.FiscalYearResponseDTO;
import com.alf.accounts_service.mappers.FiscalYearMapper;
import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.core.FiscalYear;
import com.alf.accounts_service.repositories.AccountRepository;
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
public class FiscalYearService {


    private final FiscalYearRepository fiscalYearRepository;
    private final AccountRepository accountRepository;
    private final FiscalYearMapper fiscalYearMapper;
    private final IdGeneratorService idGeneratorService ;
    private final AuditorAware<String> auditorAware;


    @Transactional
    public FiscalYearResponseDTO createFiscalYear(FiscalYearRequestDTO dto) throws ValidationException {
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new ValidationException("End date must be after start date.");
        }
        fiscalYearRepository.findByNameAndAccountId(dto.getName(), dto.getAccountId())
                .ifPresent(fy -> {
                    try {
                        throw new ValidationException("Fiscal year with this name already exists for the account.");
                    } catch (ValidationException e) {
                        throw new RuntimeException(e);
                    }
                });

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + dto.getAccountId()));

        FiscalYear fiscalYear = fiscalYearMapper.toEntity(dto);
        fiscalYear.setAccount(account);

        String serialId = idGeneratorService.generateNextId("fiscal_year");

        fiscalYear.setSerialId(serialId);

        fiscalYear.setClosed(false); // Default

        return fiscalYearMapper.toResponseDTO(fiscalYearRepository.save(fiscalYear));
    }

    @Transactional(readOnly = true)
    public FiscalYearResponseDTO getFiscalYearById(Integer id) {
        return fiscalYearRepository.findById(id)
                .map(fiscalYearMapper::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Year not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<FiscalYearResponseDTO> getAllFiscalYearsByAccountId(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Account not found with id: " + accountId);
        }
        return fiscalYearRepository.findByAccountIdOrderByNameAsc(accountId).stream()
                .map(fiscalYearMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FiscalYearResponseDTO updateFiscalYear(Integer id, FiscalYearRequestDTO dto) throws ValidationException {
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new ValidationException("End date must be after start date.");
        }

        FiscalYear existingFiscalYear = fiscalYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Year not found with id: " + id));

        // Check for name uniqueness if name or accountId is changed
        if (!existingFiscalYear.getName().equals(dto.getName()) || !existingFiscalYear.getAccount().getId().equals(dto.getAccountId())) {
            if (fiscalYearRepository.existsByAccountIdAndIdNotAndName(dto.getAccountId(), id, dto.getName())) {
                throw new ValidationException("Another fiscal year with this name already exists for the selected account.");
            }
        }

        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + dto.getAccountId()));

        fiscalYearMapper.updateEntityFromDto(dto, existingFiscalYear);
        existingFiscalYear.setAccount(account); // Update account if changed

        return fiscalYearMapper.toResponseDTO(fiscalYearRepository.save(existingFiscalYear));
    }

    @Transactional
    public FiscalYearResponseDTO closeFiscalYear(Integer id) {
        FiscalYear fiscalYear = fiscalYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Year not found with id: " + id));
        // Add logic: cannot close if it has open periods
        // if (fiscalPeriodRepository.existsByFiscalYearIdAndClosedFalse(id)) {
        //     throw new ValidationException("Cannot close fiscal year with open periods.");
        // }
        fiscalYear.setClosed(true);
        return fiscalYearMapper.toResponseDTO(fiscalYearRepository.save(fiscalYear));
    }

    @Transactional
    public FiscalYearResponseDTO openFiscalYear(Integer id) {
        FiscalYear fiscalYear = fiscalYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Year not found with id: " + id));
        fiscalYear.setClosed(false);
        return fiscalYearMapper.toResponseDTO(fiscalYearRepository.save(fiscalYear));
    }


    @Transactional
    public void deleteFiscalYear(Integer id) {
        FiscalYear fiscalYear = fiscalYearRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fiscal Year not found with id: " + id));
        if(fiscalYear.isDeleted())
            return;

        fiscalYear.setDeleted(true);
        fiscalYear.setDeletedDate(LocalDateTime.now());
        fiscalYear.setDeletedBy(auditorAware.getCurrentAuditor().orElse("system"));
        fiscalYearRepository.save(fiscalYear);
    }
}
