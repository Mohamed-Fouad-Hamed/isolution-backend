package com.alf.core_common.dtos.user;

public record UserRegisteredEvent(  String accountId,
                                    String userId,
                                    String fullName,
                                    String email ) {
}
