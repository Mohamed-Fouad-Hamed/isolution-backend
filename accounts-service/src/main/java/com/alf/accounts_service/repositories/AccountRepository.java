package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.Account;
import com.alf.accounts_service.models.enums.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findBySerialId(String serialId);

    List<Account> findByCompanyTypeNotIn(List<CompanyType> types);
    List<Account> findByParent(Account parent);
    @Query("""
        select a from Account a
        left join fetch a.parent
    """)
    List<Account> findAllWithParent();
    boolean existsByOwnerId(String ownerId);
    boolean existsById(Long id);
    boolean existsBySerialId(String serialId);

   // List<Account> findByFactoryAndAccountType( boolean is_factory, AccountType accountType);
   // List<Account> findByAccountType( AccountType accountType);
   // List<Account> findByFactoryAndAccountTypeAndIdNotIn(  boolean is_factory,AccountType accountType , List<Long> ids );
}
