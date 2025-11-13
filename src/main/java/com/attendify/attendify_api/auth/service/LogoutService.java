package com.attendify.attendify_api.auth.service;

import org.springframework.stereotype.Service;

import com.attendify.attendify_api.auth.model.LogoutResult;
import com.attendify.attendify_api.auth.repository.TokenRepository;
import com.attendify.attendify_api.shared.jwt.SecurityConstants;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService {
    private final TokenRepository tokenRepository;

    @Transactional
    public LogoutResult logout(HttpServletRequest request) {
        final String authenticationHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(SecurityConstants.BEARER_PREFIX))
            return LogoutResult.INVALID_HEADER;

        final String jwt = authenticationHeader.substring(SecurityConstants.BEARER_PREFIX.length());

        var refreshTokenOpt = tokenRepository.findByToken(jwt);
        if (refreshTokenOpt.isEmpty())
            return LogoutResult.TOKEN_NOT_FOUND;

        var refreshToken = refreshTokenOpt.get();
        if (refreshToken.getRevoked() || refreshToken.getExpired())
            return LogoutResult.ALREADY_REVOKED;

        refreshToken.setRevoked(true);
        refreshToken.setExpired(true);
        tokenRepository.save(refreshToken);

        return LogoutResult.SUCCESS;
    }
}
