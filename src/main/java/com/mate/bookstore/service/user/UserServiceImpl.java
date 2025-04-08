package com.mate.bookstore.service.user;

import com.mate.bookstore.dto.user.UserDto;
import com.mate.bookstore.dto.user.UserRegistrationRequestDto;
import com.mate.bookstore.exception.RegistrationException;
import com.mate.bookstore.mapper.UserMapper;
import com.mate.bookstore.model.User;
import com.mate.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        validateRegistration(userRegistrationRequestDto);

        User newUser = userMapper.toModel(userRegistrationRequestDto);
        newUser.setPassword(passwordEncoder.encode(userRegistrationRequestDto.password()));
        newUser = userRepository.save(newUser);

        log.info("Successfully registered user with Email: {}", newUser.getEmail());
        return userMapper.toDto(newUser);
    }

    private void validateRegistration(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.findByEmail(userRegistrationRequestDto.email()).isPresent()) {
            throw new RegistrationException("User with email "
                    + userRegistrationRequestDto.email()
                    + " already exists");
        }
    }
}
