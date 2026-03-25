package com.alf.accounts_service.services;


import com.alf.accounts_service.dtos.account.AccountCompany;
import com.alf.accounts_service.dtos.account.AccountDto;
import com.alf.accounts_service.dtos.account.AccountOptionReq;
import com.alf.accounts_service.dtos.company.CompanyHierarchyDto;
import com.alf.accounts_service.mappers.AccountMapper;
import com.alf.accounts_service.models.*;
import com.alf.accounts_service.models.core.Partner;
import com.alf.accounts_service.models.enums.CompanyType;
import com.alf.accounts_service.models.enums.PartnerType;
import com.alf.accounts_service.models.enums.ResourceType;
import com.alf.accounts_service.models.enums.ResourceUsage;
import com.alf.accounts_service.repositories.AccountPaymentRepository;
import com.alf.accounts_service.repositories.AccountRepository;
import com.alf.accounts_service.repositories.PartnerRepository;
import com.alf.accounts_service.repositories.PaymentRepository;
import com.alf.security.common.exceptions.ApplicationException;
import com.alf.security.common.exceptions.BusinessException;
import com.alf.security.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PartnerRepository partnerRepository;
    private final AccountPaymentRepository accountPaymentRepository;

    private final PaymentRepository paymentRepository;
    private final AccountMapper accountMapper;
    private final IdGeneratorService idGeneratorService ;

    private final ResourceService resourceService;

    private final ResourceType resourceType = ResourceType.IMAGE;

    private final ResourceUsage resourceUsage = ResourceUsage.PROFILE_PIC ;

    private  final String ownerEntity ="Company";


    public Long getAccountId(String serialId){
        Long accountId = 0L ;
        Account account = accountRepository.findBySerialId(serialId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", serialId));
        accountId = account.getId();
        return accountId;
    }

    public Account getAccount(String serialId){
        return accountRepository.findBySerialId(serialId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", serialId));
    }
    public AccountDto getAccount(Long accountId){
        Account account = accountRepository.findById(accountId).orElseThrow(()-> new ApplicationException("Unknown_Account" , HttpStatus.NOT_FOUND));
        return accountMapper.toAccountDto(account);
    }

    public List<AccountDto> getAccounts() {

        List<CompanyType> excludedTypes = List.of(
                CompanyType.SYSTEM
        );

        List<Account> accounts = accountRepository.findByCompanyTypeNotIn(excludedTypes);

        List<AccountDto> accountDtoList = accounts.stream().map( account -> {
                  AccountDto accountDto = accountMapper.toAccountDto(account);
                    ResourceFile resourceFile
                            = resourceService.getResource(account.getId(),this.resourceType,this.resourceUsage,this.ownerEntity);
                    if(resourceFile != null)
                        accountDto.setImageUrl(resourceFile.getPath());
                  return  accountDto;
                }
              ).toList();

        return  accountDtoList ;
    }

    @Transactional
    public boolean deleteAccount(String serialId){


        Account account = accountRepository.findBySerialId(serialId).orElseThrow(()->  new ResourceNotFoundException("ACCOUNT_NOT_FOUND"));

        if(null != account){

            List<Account> childList = accountRepository.findByParent(account);

            if(!childList.isEmpty())
                throw new BusinessException("ACCOUNT_HAS_TRANSACTIONS");

            /* we can be valid on some constraints for related entities */
            /*                                                          */
        }

        try {

            ResourceFile resourceFile
                    = resourceService.getResource(account.getId(), this.resourceType, this.resourceUsage, this.ownerEntity);

            if( resourceFile != null)
                resourceFile.setActive(false);

         if(account.getPartner() != null)
            account.getPartner().setDeleted(true);

            account.setDeleted(true);

            return true;

        }catch (RuntimeException ex){
            throw ex;
        } catch (Exception ex){
            throw new BusinessException("ACCOUNT_DELETE_FAILED");
        }

    }

    public boolean isExists(Long accountId){
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        return accountOptional.isPresent();

    }

    public Map<String,Long> updateAccount(AccountCompany accountCompany){

        Map<String,Long> idsMap = new HashMap<>();

        idsMap =  recursionSaveAccount(accountCompany , null , idsMap);

         return idsMap;
    }


    private Map<String,Long> recursionSaveAccount( AccountCompany accountCompany , Account parent ,  Map<String,Long> idsMap ){

        Account instance = saveAccountCompany(accountCompany,parent);

        idsMap.put( accountCompany.tempId() , instance.getId() );

        Arrays.stream(accountCompany.branches()).forEach((acc)-> {

            Account child = saveAccountCompany(acc,instance);

            idsMap.put( acc.tempId() , child.getId() );

            Arrays.stream(acc.branches()).forEach((branch)->recursionSaveAccount(branch , child , idsMap));

        });

      return idsMap;
    }

    private Account saveAccountCompany(AccountCompany accountCompany,Account parentAccount){

        Account account = null;
        Account parent = parentAccount ;
        AccountDto accountDto = null;

        if(accountCompany.serialId() == null || accountCompany.serialId() == "") {
            account = new Account();
            String serial = idGeneratorService.generateNextId("app_accounts");
            account.setSerialId(serial);
        }
        else
            account = accountRepository.findBySerialId(accountCompany.serialId())
                    .orElseThrow(() -> new ApplicationException("Unknown_Account", HttpStatus.NOT_FOUND));

        if(parent == null) {
            parent = accountCompany.parentId() == null ? null : accountRepository.findById(accountCompany.parentId()).orElse(null);
        }


        account.setOwnerId(accountCompany.ownerId());
        account.setAccount_name(accountCompany.accountName() == null ? "Empty name" : accountCompany.accountName() );
        account.setCompanyType(CompanyType.COMPANY);
        account.setTaxIdentificationNumber(accountCompany.taxIdentificationNumber());
        account.setCommercialRegistry(accountCompany.commercialRegistry());
        account.setAddress(accountCompany.address());
        account.setPhone(accountCompany.phone());
        account.setEmail(accountCompany.email());
        account.setWebsite(accountCompany.website());
        account.setStateId(accountCompany.stateId());
        account.setCountryId(accountCompany.countryId());
        account.setCurrencyId(accountCompany.currencyId());
        account.setAccountColor(accountCompany.color_account());
        account.setParent(parent);

        Partner partner = null;

        if(account.getPartner() != null){
            partner = partnerRepository.findById(account.getPartner().getId())
                    .orElseThrow(() -> new ApplicationException("Unknown_Partner", HttpStatus.NOT_FOUND));
        } else {
            partner = new Partner();
            String serial = idGeneratorService.generateNextId("partner");
            partner.setSerialId(serial);
        }
        partner.setName(accountCompany.accountName());
        partner.setDisplayName(accountCompany.accountName());
        partner.setIsCompany(true);
        partner.setCompanyType(CompanyType.COMPANY);
        partner.setType(PartnerType.CONTACT);

        // commercial partner points to itself
        partner.setCommercialPartner(partner);

        partnerRepository.save(partner);

        account.setPartner(partner);

        Account accountUpdated = accountRepository.save(account);

        return  accountUpdated ;

    }


    public AccountDto updateAccountOption(AccountOptionReq accountOptionReq){

            Account account = accountRepository.findById(accountOptionReq.id())
                    .orElseThrow(()-> new ApplicationException("Unknown_Account" , HttpStatus.NOT_FOUND));
            AccountDto accountDto = null;
            account.setCurrencyId(accountOptionReq.currency_id());
           /* Set<AccountPayment> accountPaymentSet = accountOptionReq
                    .paymentTypes().stream().map((_payment)->{
                        AccountPaymentKey accountPaymentKey = new AccountPaymentKey();
                        accountPaymentKey.setAccountId(_payment.accountId());
                        accountPaymentKey.setPaymentId(_payment.paymentId());
                        Payment payment  = paymentRepository.findById(_payment.paymentId())
                                .orElseThrow(()-> new AppExecption("Unknown_Payment" , HttpStatus.NOT_FOUND));
                        AccountPayment accountPayment =  new AccountPayment();
                        accountPayment.setId(accountPaymentKey);
                        accountPayment.setAccount(account);
                        accountPayment.setPayment(payment);
                        accountPayment.setWallet_number(_payment.wallet_number());
                        accountPayment.setWallet_password(_payment.wallet_password());
                        accountPayment.setCard_holder_name(_payment.card_holder_name());
                        accountPayment.setAccount_number(_payment.account_number());
                        accountPayment.setExpiry_month(_payment.expiry_month());
                        accountPayment.setExpiry_year(_payment.expiry_year());
                        accountPayment.setCvc(_payment.cvc());
                        accountPaymentRepository.save(accountPayment);
                        return accountPayment ;
                    }).collect(Collectors.toSet());
            account.setAccountPaymentSet(accountPaymentSet); */
            Account accountUpadted = accountRepository.save(account);
            accountDto = accountMapper.toAccountDto(accountUpadted);


        return accountDto;
    }

    public boolean deletePaymentMethod(Long accountId,Integer paymentId){
        AccountPaymentKey accountPaymentKey = new AccountPaymentKey();
        accountPaymentKey.setAccountId(accountId);
        accountPaymentKey.setPaymentId(paymentId);
        accountPaymentRepository.deleteById(accountPaymentKey);
        return true;
    }


    @Transactional(readOnly = true)
    public List<CompanyHierarchyDto> findAllHierarchy() {

        List<Account> accounts = accountRepository.findAllWithParent();

        // 1️⃣ map id → dto
        Map<Long, CompanyHierarchyDto> dtoMap = new HashMap<>();

        for (Account acc : accounts) {
            dtoMap.put(acc.getId(), accountMapper.toHierarchyDto(acc));
        }

        // 2️⃣ build tree
        List<CompanyHierarchyDto> roots = new ArrayList<>();

        for (Account acc : accounts) {
            CompanyHierarchyDto current = dtoMap.get(acc.getId());

            if (acc.getParent() != null) {
                CompanyHierarchyDto parent =
                        dtoMap.get(acc.getParent().getId());
                parent.getBranches().add(current);
            } else {
                roots.add(current);
            }
        }

        return roots;
    }


}
