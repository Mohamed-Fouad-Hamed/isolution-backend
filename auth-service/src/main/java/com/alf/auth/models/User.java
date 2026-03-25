package com.alf.auth.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "APP_USERS")
public class User  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_users_id_seq")
    @SequenceGenerator(name = "app_users_id_seq", sequenceName = "app_users_id_seq", allocationSize=1)
    private Long id;

    @Column(name="first_name",nullable = false, length = 50)
    private String firstName;

    @Column(name="last_name",nullable = false , length = 50)
    private String lastName;

    @Column(name="email", length = 50)
    private String email;

    @Column(name="phone", length = 30)
    private String phone;
    @Column(name="login" ,nullable = false, length = 50)
    private String login;

    @Column(name="secret_word",nullable = false, length = 100)
    private String password;

    @Column(name="s_cut",nullable = false, length = 20)
    private String s_cut = "ar";

    @Column( name="user_avatar", length = 255 )
    private String user_avatar;

    @Column( name="user_image", length = 255 )
    private String user_image;

    @Column(name = "is_otp_required" )
    private Boolean isOtpRequired;

    @Column(name="partner_id")
    private String partnerId;

    @Column(name = "account_id")
    private String accountId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorityNames = new HashSet<>();;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<>();

 /*   @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_allow_accounts",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "id")})
    private Set<Account> accounts = new HashSet<>();*/


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();

        roles.stream().forEach( a -> {
            authorities.add( new SimpleGrantedAuthority(a.getName()));
            a.getPermissions().stream().forEach(p -> { authorities.add( new SimpleGrantedAuthority(p.getName()));});
        });

        return authorities;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
