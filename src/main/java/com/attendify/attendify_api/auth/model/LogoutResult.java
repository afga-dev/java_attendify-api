package com.attendify.attendify_api.auth.model;

public enum LogoutResult {
    SUCCESS,
    TOKEN_NOT_FOUND,
    ALREADY_REVOKED,
    INVALID_HEADER
}
