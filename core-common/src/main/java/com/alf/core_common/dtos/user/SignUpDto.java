package com.alf.core_common.dtos.user;

public record SignUpDto(

        String accountId,
        String firstName,
        String lastName,
        String email,
        String login,
        String password,
        String s_cut,
        String phone
) {
}
