package com.alf.accounts_service.repositories;


import com.alf.accounts_service.dtos.country.CountryStateJoinDto;
import com.alf.accounts_service.models.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Integer>{
    Optional<Country> findByName(String name);

    List<Country> findByNameContainingIgnoreCase(String name);

    List<Country> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Long countByNameContainingIgnoreCase(String name);

    @Query("SELECT new com.alf.accounts_service.dtos.country.CountryStateJoinDto(c.id, c.name, s.id, s.name) " +
            "FROM Country c LEFT JOIN c.states s ORDER BY c.name, s.name")
    List<CountryStateJoinDto> findAllCountriesWithStates();

    @Query("SELECT c FROM Country c LEFT JOIN FETCH c.states WHERE c.id = :id")
    Optional<Country> findByIdWithStates(@Param("id") Integer id);

    @Query("SELECT COUNT(s) FROM State s WHERE s.country.id = :countryId")
    Long countStatesByCountryId(@Param("countryId") Integer countryId);
}
