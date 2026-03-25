package com.alf.auth.services;


import com.alf.auth.config.PhoneProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PhoneService {

    @Autowired
    private PhoneProviderConfig phoneProviderConfig;

    public void sendSMS(String toPhone,String smsContent){

       /* Twilio.init(phoneProviderConfig.getSid(), phoneProviderConfig.getToken());
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(toPhone),
                        new com.twilio.type.PhoneNumber(phoneProviderConfig.getPhone()),
                        smsContent)
                .create();
        System.out.println(message.getSid());
*/

    }
}
