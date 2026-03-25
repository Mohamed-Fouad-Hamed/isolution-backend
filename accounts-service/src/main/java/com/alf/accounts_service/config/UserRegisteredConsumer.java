package com.alf.accounts_service.config;

import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.core.Partner;
import com.alf.accounts_service.models.enums.CompanyType;
import com.alf.accounts_service.models.enums.PartnerType;
import com.alf.accounts_service.repositories.AccountRepository;
import com.alf.accounts_service.repositories.PartnerRepository;
import com.alf.accounts_service.services.IdGeneratorService;
import com.alf.core_common.dtos.user.UserRegisteredEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserRegisteredConsumer {

    private final AccountRepository accountRepo;
    private final PartnerRepository partnerRepository;

    private final IdGeneratorService idGeneratorService;
    private final ObjectMapper objectMapper;

    private static final Logger log = LoggerFactory.getLogger(UserRegisteredConsumer.class);


    @KafkaListener(topics = "UserRegisteredEvent" , groupId = "accounts-service")
    @Transactional
    public void handle(String payload) throws JsonProcessingException {

        UserRegisteredEvent event =
                objectMapper.readValue(payload, UserRegisteredEvent.class);

        //  Idempotency check
        if (accountRepo.existsByOwnerId(event.userId())) {
            return;
        }

        try {

            Account account = new Account();
            account.setSerialId(event.accountId());
            account.setOwnerId(event.userId());
            account.setAccount_name(event.fullName());
            account.setCompanyType(CompanyType.COMPANY);
            account.setEmail(event.email());
            account.setCreatedBy(event.userId());
            account.setCreatedDate(LocalDateTime.now());
            account.setLastModifiedBy(event.userId());
            account.setLastModifiedDate(LocalDateTime.now());
            account.setParent(null);

            Partner partner = new Partner();
            String serial = idGeneratorService.generateNextId("partner");
            partner.setSerialId(serial);
            partner.setName(event.fullName());
            partner.setDisplayName(event.fullName());
            partner.setIsCompany(true);
            partner.setCompanyType(CompanyType.COMPANY);
            partner.setType(PartnerType.CONTACT);
            partner.setCreatedBy(event.userId());
            partner.setCreatedDate(LocalDateTime.now());
            partner.setLastModifiedBy(event.userId());
            partner.setLastModifiedDate(LocalDateTime.now());

            // commercial partner points to itself
            partner.setCommercialPartner(partner);

            Partner newPartnerSaved =  partnerRepository.save(partner);

            account.setPartner(newPartnerSaved);

            accountRepo.save(account);

        }catch (Exception ex){
            log.info(" Error Account Registration  ------> " + ex.getMessage());
            throw  ex;
        }
    }
}
