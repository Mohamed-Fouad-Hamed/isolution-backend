package com.alf.core_common.dtos.payload;

public record MessageResponse(
         String message ,
         int id ,
         int status  ,
         Object entity ,
         Object list
) {
}
