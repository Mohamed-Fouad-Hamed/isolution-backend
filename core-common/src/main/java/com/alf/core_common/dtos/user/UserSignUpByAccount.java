package com.alf.core_common.dtos.user;

public record UserSignUpByAccount(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String login,
        String password,
        String s_cut
) {
}
