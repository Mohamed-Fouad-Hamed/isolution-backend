package com.alf.core_common.dtos.user;

public record TokenResponse(String accessToken, String refreshToken, long expiresInSeconds) {}
