package com.alf.auth.config;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
@RequiredArgsConstructor
@Component
public class DateTimeExpiredChecker {


    public String getTimeAfterDay(){
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date expiredDate = cal.getTime() ;
        return String.valueOf(expiredDate.getTime());
    }

    public  boolean validWithCurrentDateExpired(String expiredDate){
        Long expiredTime = Long.valueOf(expiredDate);
        return (new Date().getTime() > expiredTime);
    }
}
