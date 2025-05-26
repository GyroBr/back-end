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
import org.springframework.web.context.request.RequestAttributes;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RequestAttributes requestAttributes;


    public CompanyResponseDTO createCompany(CompanyRequestDTO companyRequestDTO) {
        User user = userService.createUser(new UserRequestDTO(companyRequestDTO.name(), companyRequestDTO.email(), companyRequestDTO.password(), Roles.ROLE_ADMIN));
        Address address = new Address(companyRequestDTO.address());
        Company company = new Company(companyRequestDTO);
        company.setUser(user);
        company.setAddress(address);

        return new CompanyResponseDTO(companyRepository.save(company));

    }

    public CompanyResponseDTO updateCompany(Long companyId, CompanyRequestDTO companyRequestDTO) {
        var company = existsCompanyId(companyId);
        var newCompany = updateCompanyFactory(company, companyRequestDTO);
        var newUser = userService.updateUser(company, newCompany, Roles.ROLE_ADMIN);
        newCompany.setUser(newUser);
        return new CompanyResponseDTO(companyRepository.save(newCompany));

    }

    public CompanyResponseDTO getCompanyById(Long id) {
        return new CompanyResponseDTO(existsCompanyId(id));
    }


    public Company existsCompanyId(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new NotFoundException("company not found"));
    }

    private Company updateCompanyFactory(Company company, CompanyRequestDTO companyRequestDTO) {
        var newCompany = new Company(companyRequestDTO);
        newCompany.setEmployees(company.getEmployees());
        newCompany.setTotalRevenuOfSales(company.getTotalRevenuOfSales());
        newCompany.setOrders(company.getOrders());
        newCompany.setCombos(company.getCombos());
        newCompany.setProducts(company.getProducts());
        if (!companyRequestDTO.address().equals(company.getAddress())) {
            newCompany.setAddress(new Address(companyRequestDTO.address()));
        } else {
            newCompany.setAddress(company.getAddress());
        }

        newCompany.setId(company.getId());
        return newCompany;
    }


}
