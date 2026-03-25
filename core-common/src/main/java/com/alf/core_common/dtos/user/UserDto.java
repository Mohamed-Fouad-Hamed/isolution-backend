package com.alf.core_common.dtos.user;

public record UserDto(
        String accountId,
        String firstName,
        String lastName,
        String phone,
        String email,
        String login,
        String s_cut
) {
}
