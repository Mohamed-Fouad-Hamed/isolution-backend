package com.alf.auth.OTPServices;


import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OTPGenerator {


    public Integer generateOTP()
    {
        Random random = new Random();
        int OTP = 100000 + random.nextInt(900000);
        return OTP;
    }
}
