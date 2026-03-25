package com.alf.auth.security;

import com.alf.auth.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationImpl implements Authentication {

    private User user = null;
    private boolean isAuth = false;

    public CustomAuthenticationImpl(User currentUser){
        this.user = currentUser;
        this.isAuth = user != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuth;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
          this.isAuth = isAuthenticated;
    }

    @Override
    public String getName() {
        return user.getLogin();
    }
}
