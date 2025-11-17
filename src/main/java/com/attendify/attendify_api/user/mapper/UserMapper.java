package com.attendify.attendify_api.user.mapper;

import org.springframework.stereotype.Component;

import com.attendify.attendify_api.user.dto.UserSummaryDTO;
import com.attendify.attendify_api.user.model.User;

@Component
public class UserMapper {
    public UserSummaryDTO toSummary(User user) {
        return UserSummaryDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
