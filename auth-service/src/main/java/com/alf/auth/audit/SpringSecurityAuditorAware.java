package com.alf.auth.audit;

import com.alf.auth.models.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.Optional;

//@Component("auditorAware") // Bean name can be referenced if needed
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            // Handle cases where there is no logged-in user (e.g., system processes)
            return Optional.of("system"); // Or Optional.empty() if preferred
        }

        // Assuming the principal's name is the username you want to store

        User user = (User) authentication.getPrincipal();
        return Optional.of(user.getLogin());

        // If using a custom UserDetails object:
        // return Optional.of(((YourUserDetails) authentication.getPrincipal()).getUsername());
    }
}
