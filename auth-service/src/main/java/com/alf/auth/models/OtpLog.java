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
@Entity
@Table(name = "OTP_LOGS")
public class OtpLog {

    @Column(name = "login",nullable = false, length = 50)
    @Id
    private String login;

    @Column(name = "otp_token",nullable = false, length = 10)
    private String otpToken;

    @Column(name = "expire_time",nullable = false, length = 30)
    private String expireTime;

    @Column(name = "otp_valid",nullable = false)
    private boolean otpValid = false;


}
