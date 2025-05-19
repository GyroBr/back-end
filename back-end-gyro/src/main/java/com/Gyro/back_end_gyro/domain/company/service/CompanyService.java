package com.Gyro.back_end_gyro.domain.company.service;

import com.Gyro.back_end_gyro.domain.address.entity.Address;
import com.Gyro.back_end_gyro.domain.company.dto.CompanyRequestDTO;
import com.Gyro.back_end_gyro.domain.company.dto.CompanyResponseDTO;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.company.repository.CompanyRepository;
import com.Gyro.back_end_gyro.domain.user.dto.UserRequestDTO;
import com.Gyro.back_end_gyro.domain.user.entity.User;
import com.Gyro.back_end_gyro.domain.user.enums.Roles;
import com.Gyro.back_end_gyro.domain.user.service.UserService;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    public CompanyResponseDTO createCompany(CompanyRequestDTO companyRequestDTO) {
        User user = userService.createUser(new UserRequestDTO(companyRequestDTO.name(), companyRequestDTO.email(), companyRequestDTO.password(), Roles.ROLE_ADMIN));
        Address address = new Address(companyRequestDTO.address());
        Company company = new Company(companyRequestDTO);
        company.setPassword(passwordEncoder.encode(companyRequestDTO.password()));
        company.setUser(user);
        company.setAddress(address);

        return new CompanyResponseDTO(companyRepository.save(company));

    }

    public CompanyResponseDTO updateCompany(Long companyId, CompanyRequestDTO companyRequestDTO) {
        var company = existsCompanyId(companyId);
        var user = company.getUser();
        var updateCompany = new Company(companyRequestDTO);
        var updaptedUser = userService.createUser(new UserRequestDTO(updateCompany.getName(), updateCompany.getEmail(), updateCompany.getPassword(), Roles.ROLE_ADMIN));
        updateCompany.setId(companyId);
        updaptedUser.setId(user.getId());
        return new CompanyResponseDTO(companyRepository.save(company));

    }

    public CompanyResponseDTO getCompanyById(Long id) {
        return new CompanyResponseDTO(existsCompanyId(id));
    }


    public Company existsCompanyId(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new NotFoundException("company not found"));
    }


}
