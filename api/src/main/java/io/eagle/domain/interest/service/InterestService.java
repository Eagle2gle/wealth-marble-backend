package io.eagle.domain.interest.service;

import io.eagle.domain.interest.dto.InterestDto;
import io.eagle.domain.interest.repository.InterestRepository;
import io.eagle.entity.Interest;
import io.eagle.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;

    public List<InterestDto> getAllInterest(User user) {
        return interestRepository.findAllByUser(user).stream().map(interest -> new InterestDto(interest)).collect(Collectors.toList());
    }

}
