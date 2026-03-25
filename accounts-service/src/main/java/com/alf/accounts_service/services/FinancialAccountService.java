package com.alf.accounts_service.services;

import com.alf.accounts_service.dtos.financial.FinancialAccountCreateDto;
import com.alf.accounts_service.dtos.financial.FinancialAccountDto;
import com.alf.accounts_service.dtos.financial.FinancialAccountUpdateDto;
import com.alf.accounts_service.dtos.partner.CursorPageResponse;
import com.alf.accounts_service.dtos.partner.PartnerCursor;
import com.alf.accounts_service.dtos.partner.PartnerLookupDto;
import com.alf.accounts_service.dtos.type.CursorPageTypeResponse;
import com.alf.accounts_service.dtos.type.TypeCursor;
import com.alf.accounts_service.dtos.type.TypeLookupDto;
import com.alf.accounts_service.mappers.FinancialAccountMapper;
import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.coa.FinancialAccount;
import com.alf.accounts_service.models.coa.FinancialAccountType;
import com.alf.accounts_service.models.core.Partner;
import com.alf.accounts_service.repositories.AccountRepository;
import com.alf.accounts_service.repositories.FinancialAccountRepository;
import com.alf.accounts_service.repositories.FinancialAccountTypeRepository;
import com.alf.security.common.exceptions.DataConflictException;
import com.alf.security.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FinancialAccountService {

    private final AuditorAware<String> auditorAware;
    private static final Logger log = LoggerFactory.getLogger(FinancialAccountService.class);

    private final FinancialAccountRepository financialAccountRepository;

    private final AccountService accountService;
    private final FinancialAccountTypeRepository financialAccountTypeRepository;
    private final FinancialAccountMapper financialAccountMapper;

    private final IdGeneratorService idGeneratorService ;

    private Long getAccountId(String serialId){
        return accountService.getAccountId(serialId);
    }
    private Account getAccount(String serialId){
        return accountService.getAccount(serialId);
    }

    public FinancialAccountDto createFinancialAccount(String serialAccountId, FinancialAccountCreateDto createDto) {
        log.info("Attempting to create financial account with serialId '{}' for accountId {}", createDto.getAccountTypeId(), serialAccountId);

        // 1. Validate parent Account exists
        Account parentAccount = getAccount(serialAccountId);

        // 2. Validate FinancialAccountType exists
        FinancialAccountType accountType = financialAccountTypeRepository.findById(createDto.getAccountTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("FinancialAccountType", "id", createDto.getAccountTypeId()));

        // 3. Validate Serial ID uniqueness (globally and within the Account scope for belt-and-braces)
      /*
        if (financialAccountRepository.existsBySerialId(createDto.getSerialId())) {
            throw new DataConflictException("Financial Account with Serial ID '" + createDto.getSerialId() + "' already exists globally.");
        }
        if (financialAccountRepository.existsBySerialIdAndAccountId(createDto.getSerialId(), accountId)) {
            throw new DataConflictException("Financial Account with Serial ID '" + createDto.getSerialId() + "' already exists for Account " + accountId);
        }
      */

        // 4. Validate Parent Serial ID exists within the same Account (if provided)
        /*
        if (StringUtils.hasText(createDto.getParentSerialId())) {
            financialAccountRepository.findBySerialIdAndAccountId(createDto.getParentSerialId(), accountId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Parent Financial Account with Serial ID '%s' not found within Account %d",
                                    createDto.getParentSerialId(), accountId),
                            "serialId/accountId", createDto.getParentSerialId() + "/" + accountId));
            // Prevent self-parenting
            if (Objects.equals(createDto.getSerialId(), createDto.getParentSerialId())) {
                throw new DataConflictException("Financial Account cannot be its own parent.");
            }
        }
       */
        // 5. Map DTO to Entity
        FinancialAccount financialAccount = financialAccountMapper.toEntity(createDto);

        String serialId = idGeneratorService.generateNextId("financial_account");

        financialAccount.setSerialId(serialId);

        // 6. Set relationships and defaults
        financialAccount.setAccount(parentAccount);
        financialAccount.setAccountType(accountType);
        // Defaults are set in entity or DTO, but can be enforced here if needed
        financialAccount.setBalance(BigDecimal.ZERO); // Ensure balance starts at zero
        financialAccount.setCurrencyId(createDto.getCurrencyId() );
        financialAccount.setIsActive(createDto.getIsActive() != null ? createDto.getIsActive() : true);
        financialAccount.setDebit(createDto.isDebit()); // Assuming default is handled in DTO/entity
        financialAccount.setCashAccount(createDto.isCashAccount());
        financialAccount.setBankAccount(createDto.isBankAccount());
        financialAccount.setControlAccount(createDto.isControlAccount());


        // 7. Save
        FinancialAccount savedAccount = financialAccountRepository.save(financialAccount);
        log.info("Successfully created financial account with id {} for accountId {}", savedAccount.getId(), serialAccountId);

        // 8. Map back to DTO
        return financialAccountMapper.toDto(savedAccount);
    }


    @Transactional(readOnly = true)
    public List<FinancialAccountDto> getAllFinancialAccountsByAccount(String serialAccountId) {
        Long accountId = getAccountId(serialAccountId);
        List<FinancialAccount> accounts = financialAccountRepository.findByAccountId(accountId);
        return financialAccountMapper.toDtoList(accounts);
    }


    @Transactional(readOnly = true)
    public List<FinancialAccountDto> getActiveFinancialAccountsByAccount(String serialAccountId) {
        Long accountId = getAccountId(serialAccountId);
        List<FinancialAccount> accounts = financialAccountRepository.findByAccountIdAndIsActiveTrue(accountId);
        return financialAccountMapper.toDtoList(accounts);
    }


    @Transactional(readOnly = true)
    public Optional<FinancialAccountDto> getFinancialAccountById(String serialAccountId, Integer financialAccountId) {
        Long accountId = getAccountId(serialAccountId);
        return financialAccountRepository.findByIdAndAccountId(financialAccountId, accountId)
                .map(financialAccountMapper::toDto);
    }


    public FinancialAccountDto updateFinancialAccount(String serialAccountId, Integer financialAccountId, FinancialAccountUpdateDto updateDto) {

        Long accountId = getAccountId(serialAccountId);
        // 1. Fetch existing entity, ensuring it belongs to the correct parent Account
        FinancialAccount existingAccount = financialAccountRepository.findByIdAndAccountId(financialAccountId, accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Financial Account with ID %d not found under Account %d", financialAccountId, accountId),
                        "id/accountId", financialAccountId + "/" + accountId));

        // 2. Validate and update AccountType if changed
        FinancialAccountType accountType = existingAccount.getAccountType();
        if (!Objects.equals(accountType.getId(), updateDto.getAccountTypeId())) {
            accountType = financialAccountTypeRepository.findById(updateDto.getAccountTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("FinancialAccountType", "id", updateDto.getAccountTypeId()));
        }

        // 3. Validate Parent Serial ID if changed
        String newParentSerialId = updateDto.getParentSerialId();
        if (StringUtils.hasText(newParentSerialId) && !Objects.equals(existingAccount.getParentSerialId(), newParentSerialId)) {
            // Check new parent exists within the same Account
            financialAccountRepository.findBySerialIdAndAccountId(newParentSerialId, accountId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("New Parent Financial Account with Serial ID '%s' not found within Account %d",
                                    newParentSerialId, accountId),
                            "serialId/accountId", newParentSerialId + "/" + accountId));
            // Prevent self-parenting
            if (Objects.equals(existingAccount.getSerialId(), newParentSerialId)) {
                throw new DataConflictException("Financial Account cannot be its own parent.");
            }
            // Add cycle detection logic if necessary (more complex)
        } else if (!StringUtils.hasText(newParentSerialId) && StringUtils.hasText(existingAccount.getParentSerialId())) {
            // Handle case where parent is being removed (making it a root)
            log.debug("Setting parentSerialId to null for financial account {}", financialAccountId);
        }


        // 4. Map updatable fields from DTO to existing entity
        financialAccountMapper.updateEntityFromDto(updateDto, existingAccount);

        // 5. Set potentially changed relationship
        existingAccount.setAccountType(accountType);

        // 6. Save updated entity
        FinancialAccount updatedAccount = financialAccountRepository.save(existingAccount);
        log.info("Successfully updated financial account {}", updatedAccount.getId());

        // 7. Map back to DTO
        return financialAccountMapper.toDto(updatedAccount);
    }

    @Transactional()
    public void deleteFinancialAccount(String serialAccountId, String serialId) {

        Long accountId = getAccountId(serialAccountId);

        // 1. Verify existence and ownership
        FinancialAccount accountToDelete = financialAccountRepository.findBySerialIdAndAccountId_IgnoreSoftDelete(serialId, accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Financial Account with ID %d not found under Account %d", serialId, accountId),
                        "id/accountId", serialId + "/" + accountId));

        // 2. Pre-delete checks (BUSINESS LOGIC - IMPORTANT)
        // Check for non-zero balance
        if (accountToDelete.getBalance() != null && accountToDelete.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new DataConflictException(
                    String.format("Cannot delete financial account '%s' (%s) with a non-zero balance (%s).",
                            accountToDelete.getName(), accountToDelete.getSerialId(), accountToDelete.getBalance()));
        }

        // Check for children
        List<FinancialAccount> children = financialAccountRepository.findByParentSerialIdAndAccountId(
                accountToDelete.getSerialId(), accountId);
        if (!children.isEmpty()) {
            throw new DataConflictException(
                    String.format("Cannot delete financial account '%s' (%s) because it has child accounts. Reassign children first.",
                            accountToDelete.getName(), accountToDelete.getSerialId()));
        }

        // Check for associated transactions/journal entries (Requires JournalEntryLineRepository)
        // if (journalEntryLineRepository.existsByFinancialAccountId(financialAccountId)) {
        //     throw new DataConflictException("Cannot delete financial account because it has associated journal entries.");
        // }

        // 3. Perform deletion
        accountToDelete.setDeleted(true);
        accountToDelete.setDeletedDate(LocalDateTime.now());
        accountToDelete.setDeletedBy(auditorAware.getCurrentAuditor().orElse("system"));
    }


    @Transactional(readOnly = true)
    public List<FinancialAccountDto> getDirectChildFinancialAccounts(String serialAccountId, String parentSerialId) {
        Long accountId = getAccountId(serialAccountId);
        List<FinancialAccount> children = financialAccountRepository.findByParentSerialIdAndAccountId(parentSerialId, accountId);
        return financialAccountMapper.toDtoList(children);
    }


    @Transactional(readOnly = true)
    public List<FinancialAccountDto> getRootFinancialAccounts(String serialAccountId) {
        Long accountId = getAccountId(serialAccountId);
        List<FinancialAccount> roots = financialAccountRepository.findByAccountIdAndParentSerialIdIsNull(accountId);
        return financialAccountMapper.toDtoList(roots);
    }

    @Transactional(readOnly = true)
    public CursorPageTypeResponse<TypeLookupDto> search(
            String serialAccountId,
            String keyword,
            TypeCursor cursor,
            int size
    ) {

        Long accountId = getAccountId(serialAccountId);

        Pageable pageable = PageRequest.of(0, size);

        List<FinancialAccount> financialAccounts = financialAccountRepository.searchAfter(
                accountId,
                keyword,
                cursor != null ? cursor.getLastSearchText() : null,
                cursor != null ? cursor.getLastId() : null,
                pageable
        );

        List<TypeLookupDto> data = financialAccounts.stream()
                .map(p -> new TypeLookupDto(
                        Long.getLong(p.getId().toString()),
                        p.getName(),
                        p.getAccountType().getName()
                ))
                .toList();

        TypeCursor nextCursor = null;
        boolean hasNext = false;

        if (!financialAccounts.isEmpty()) {
            FinancialAccount last = financialAccounts.get(financialAccounts.size() - 1);

            nextCursor = new TypeCursor(
                    last.getName(),
                    Long.getLong(last.getId().toString())
            );

            hasNext = financialAccounts.size() == size;
        }

        return new CursorPageTypeResponse<>(data, nextCursor, hasNext);
    }



}