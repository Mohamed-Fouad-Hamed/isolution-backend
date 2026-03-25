package com.alf.accounts_service.repositories;

import com.alf.accounts_service.models.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Integer> {
    List<State> findByCountryId(Long countryId);

    List<State> findByNameContainingIgnoreCase(String name);

    @Query("SELECT s FROM State s JOIN FETCH s.country WHERE s.country.id = :countryId")
    List<State> findByCountryIdWithCountry(@Param("countryId") Integer countryId);

    @Query("SELECT s FROM State s JOIN FETCH s.country WHERE s.id = :id")
    State findByIdWithCountry(@Param("id") Integer id);

    boolean existsByNameAndCountryId(String name, Integer countryId);
}
