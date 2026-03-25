package com.alf.core_common.dtos.user;

public record CredentialDto(String login, char[] password , boolean rememberMe , String deviceId) {
}
