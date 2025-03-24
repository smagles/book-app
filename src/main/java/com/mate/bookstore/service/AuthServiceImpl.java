package com.mate.bookstore.service;

import com.mate.bookstore.dto.UserDto;
import com.mate.bookstore.dto.UserRegistrationRequestDto;
import com.mate.bookstore.exception.RegistrationException;
import com.mate.bookstore.mapper.UserMapper;
import com.mate.bookstore.model.User;
import com.mate.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        validateRegistration(userRegistrationRequestDto);

        User newUser = userMapper.toModel(userRegistrationRequestDto);
        newUser = userRepository.save(newUser);

        return userMapper.toDto(newUser);
    }

    private void validateRegistration(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.email()).isPresent()) {
            throw new RegistrationException("User already exists");
        }
    }
}
