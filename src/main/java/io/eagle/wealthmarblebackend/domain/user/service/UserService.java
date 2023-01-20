package io.eagle.wealthmarblebackend.domain.user.service;

import io.eagle.wealthmarblebackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;



}
