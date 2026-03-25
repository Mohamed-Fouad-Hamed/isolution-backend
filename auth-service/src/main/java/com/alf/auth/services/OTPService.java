package com.alf.auth.services;

import com.alf.auth.OTPServices.OTPGenerator;
import com.alf.core_common.dtos.email.EmailDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Description(value = "Service responsible for handling OTP related functionality.")
@Service
public class OTPService {

    private final Logger LOGGER = LoggerFactory.getLogger(OTPService.class);

    private OTPGenerator otpGenerator;
    private EmailService emailService;
    private UserService userService;
    private PhoneService phoneService;

    /**
     * Constructor dependency injector
     * @param otpGenerator - otpGenerator dependency
     * @param emailService - email service dependency
     * @param userService - user service dependency
     */
    public OTPService(OTPGenerator otpGenerator, EmailService emailService, UserService userService, PhoneService phoneService)
    {
        this.otpGenerator = otpGenerator;
        this.emailService = emailService;
        this.userService = userService;
        this.phoneService = phoneService;
    }


    public boolean sendOTP2Email(String login){
        // generate otp
        Integer otp = otpGenerator.generateOTP();
        if (otp == -1)
        {
            LOGGER.error("OTP generator is not working...");
            return  false;
        }

        LOGGER.info("Generated OTP: {}", otp);

        String userEmail = userService.findEmailByLogin(login);
        List<String> recipients = new ArrayList<>();
        recipients.add(userEmail);

        // generate emailDTO object
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSubject("ISolution OTP Code.");
        emailDTO.setBody("OTP Code: " + otp);
        emailDTO.setRecipients(recipients);

        // register in otp logs
        userService.updateOtpLog(login, String.valueOf(otp));

        // send generated e-mail
        return emailService.sendSimpleMessage(emailDTO);
    }
   public boolean sendOTP2Phone(String login){
        Integer otp = otpGenerator.generateOTP();
        if (otp == -1)
        {
            LOGGER.error("OTP generator is not working...");
            return  false;
        }
        userService.updateOtpLog(login, String.valueOf(otp));
        phoneService.sendSMS(login,  String.valueOf(otp));
        return true;
    }
    public boolean sendOTP2Email_ResetPassword(String login){
        // generate otp
        Integer otp = otpGenerator.generateOTP();
        if (otp == -1)
        {
            LOGGER.error("OTP generator is not working...");
            return  false;
        }

        LOGGER.info("Generated OTP: {}", otp);

        String userEmail = userService.findEmailByLogin(login);
        List<String> recipients = new ArrayList<>();
        recipients.add(userEmail);

        // generate emailDTO object
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setSubject("Deal App OTP Code.");
        emailDTO.setBody("OTP Code: " + otp);
        emailDTO.setRecipients(recipients);

        // register in otp logs
        userService.updateResetPasswordOtpLog(login, String.valueOf(otp));

        // send generated e-mail
        return emailService.sendSimpleMessage(emailDTO);


    }
    public boolean sendOTP2Phone_ResetPassword(String login){
        Integer otp = otpGenerator.generateOTP();
        if (otp == -1)
        {
            LOGGER.error("OTP generator is not working...");
            return  false;
        }
        userService.updateResetPasswordOtpLog(login, String.valueOf(otp));
        phoneService.sendSMS(login,  String.valueOf(otp));
        return true;
    }


}
