package com.alf.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class PatternValidation {
    final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    final String PHONE_PATTERN = "^(\\+\\d{1,3}\\d{10})";

    public boolean validEmail(String email){
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }

    public boolean validPhone(String phone){
        return Pattern.compile(PHONE_PATTERN).matcher(phone).matches();
    }
}
