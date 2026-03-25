package com.alf.auth.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "USER_LOGS")
public class UserLog {

    @Column(name = "login",nullable = false, length = 50)
    @Id
    private String login;

    @Column(name = "log_token",nullable = false, length = 255)
    private String logToken;

    @Column(name = "remember_me")
    private boolean rememberMe;

}
