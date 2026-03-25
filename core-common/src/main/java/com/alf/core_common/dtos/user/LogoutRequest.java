package com.alf.core_common.dtos.user;

public record LogoutRequest(String deviceId, String refreshToken) {}
