package com.alf.core_common.dtos.payload;

public record MessagePageableResponse(
         String message ,
         Long count ,
         int status  ,
         Object list
) {
}
