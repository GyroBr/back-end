package com.Gyro.back_end_gyro.domain.user.service;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.user.dto.UserRequestDTO;
import com.Gyro.back_end_gyro.domain.user.entity.User;
import com.Gyro.back_end_gyro.domain.user.enums.Roles;
import com.Gyro.back_end_gyro.domain.user.repository.UserRepository;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.ConflitException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
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

    public User updateUser(Employee oldEmployee, Employee newEmployee, Roles role) {
        var user = oldEmployee.getUser();

        if (!Objects.equals(oldEmployee.getEmail(), newEmployee.getEmail())) {
            User possibleUser = (User) userRepository.findUserByEmail(newEmployee.getEmail())
                    .orElse(null);

            if (possibleUser != null) {
                throw new ConflitException("Usuário ja cadastrado");
            }
        }

        user.setName(newEmployee.getName());
        user.setEmail(newEmployee.getEmail());
        user.setPassword(passwordEncoder.encode(newEmployee.getPassword()));
        user.setEmployee(newEmployee);
        user.setRoles(role);

        return userRepository.save(user);
    }

    public User updateUser(Company oldCompany, Company newCompany, Roles role) {
        var user = oldCompany.getUser();

        if (!Objects.equals(oldCompany.getEmail(), newCompany.getEmail())) {
            User possibleUser = (User) userRepository.findUserByEmail(newCompany.getEmail())
                    .orElse(null);

            if (possibleUser != null) {
                throw new ConflitException("Usuário ja cadastrado");
            }
        }

        user.setName(newCompany.getName());
        user.setEmail(newCompany.getEmail());
        user.setPassword(passwordEncoder.encode(newCompany.getPassword()));
        user.setCompany(newCompany);
        user.setRoles(role);

        return userRepository.save(user);
    }


    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public Boolean isAdmin(String role) {
        if (role.equals("ROLE_ADMIN")) {
            return true;
        }

        return false;
    }


}
