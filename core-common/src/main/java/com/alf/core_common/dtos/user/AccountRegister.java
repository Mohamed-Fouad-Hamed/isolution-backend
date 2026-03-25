package com.alf.core_common.dtos.user;

public record AccountRegister(
                              Integer account_type ,
                              String account_name,
                              String firstName,
                              String lastName,
                              String phone,
                              String email,
                              String login,
                              String password,
                              String s_cut

) {
}
