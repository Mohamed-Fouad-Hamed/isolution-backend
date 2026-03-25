package com.alf.auth.services;

import com.alf.core_common.dtos.email.EmailDTO;
import com.alf.auth.email.EmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private EmailSender emailSender;

    /**
     * Method for sending simple e-mail message.
     * @param emailDTO - data to be send.
     */
   /* public Boolean sendSimpleMessage(EmailDTO emailDTO)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("mohamed.eg.hamed@gmail.com");
        mailMessage.setTo(emailDTO.getRecipients().stream().collect(Collectors.joining(",")));
        mailMessage.setSubject(emailDTO.getSubject());
        mailMessage.setText(emailDTO.getBody());

        Boolean isSent = false;
        try
        {

            emailSender.send(mailMessage);

            isSent = true;
        }
        catch (Exception e) {
            LOGGER.error("Sending e-mail error: {}", e.getMessage());
        }
        return isSent;
    }
    */
    public Boolean sendSimpleMessage(EmailDTO emailDTO)
    {


        Boolean isSent = false;
        try
        {

            emailSender.sendPlainTextEmail("iSolutionBackEnd"
                    ,emailDTO.getRecipients().get(0)
                    ,emailDTO.getSubject()
                    , List.of(emailDTO.getBody())
                    ,true);

            isSent = true;
        }
        catch (Exception e) {
            LOGGER.error("Sending e-mail error: {}", e.getMessage());
        }
        return isSent;
    }



}
