package com.Gyro.back_end_gyro.domain.user.service;

import com.Gyro.back_end_gyro.domain.user.dto.UserRequestDTO;
import com.Gyro.back_end_gyro.domain.user.entity.User;
import com.Gyro.back_end_gyro.domain.user.repository.UserRepository;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.ConflitException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public User createUser(UserRequestDTO requestDTO) {

        User possibleUser = (User) userRepository.findUserByEmail(requestDTO.email())
                .orElse(null);

        if (possibleUser != null) {
            throw new ConflitException("Usuário ja cadastrado");
        }


        User user = new User(requestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public Boolean isAdmin(String role) {
        if (role.equals("ROLE_ADMIN")) {
            return true;
        }

        return false;
    }


}
