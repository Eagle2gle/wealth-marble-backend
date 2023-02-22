package io.eagle.domain.user.controller;

import io.eagle.auth.AuthDetails;
import io.eagle.common.ApiResponse;
import io.eagle.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/auth/users/me")
    public ApiResponse getUserInfo(
        @AuthenticationPrincipal AuthDetails authDetails
        ) {
        return ApiResponse.createSuccess(userService.getUserInfo(authDetails.getUser()));
    }

}
