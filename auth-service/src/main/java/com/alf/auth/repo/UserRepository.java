package com.alf.auth.repo;

import com.alf.auth.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByLogin(String login);

    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.login = :login")
    Optional<User> findByIdWithRoles(@Param("login") String login);

}
