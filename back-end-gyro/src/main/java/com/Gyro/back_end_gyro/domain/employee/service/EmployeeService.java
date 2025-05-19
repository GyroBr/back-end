package com.Gyro.back_end_gyro.domain.employee.service;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.employee.dto.EmployeeRequestDTO;
import com.Gyro.back_end_gyro.domain.employee.dto.EmployeeResponseDTO;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.employee.repository.EmployeeRepository;
import com.Gyro.back_end_gyro.domain.user.dto.UserRequestDTO;
import com.Gyro.back_end_gyro.domain.user.entity.User;
import com.Gyro.back_end_gyro.domain.user.enums.Roles;
import com.Gyro.back_end_gyro.domain.user.service.UserService;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    public EmployeeResponseDTO createEmployee(Company company, EmployeeRequestDTO requestDTO) {
        Employee employee = new Employee(requestDTO);
        User user = userService.createUser(new UserRequestDTO(employee.getName(), employee.getEmail(), employee.getPassword(), Roles.ROLE_EMPLOYEE));
        employee.setUser(user);
        employee.setCompany(company);
        return new EmployeeResponseDTO(employeeRepository.save(employee));
    }

    public EmployeeResponseDTO updateEmployee(Long employeeId, EmployeeRequestDTO requestDTO) {
        var employee = existsEmployeeById(employeeId);
        var user = employee.getUser();
        var updatedEmployee = new Employee(requestDTO);
        updatedEmployee.setId(employeeId);
        var updatedUser = userService.createUser(new UserRequestDTO(updatedEmployee.getName(), updatedEmployee.getEmail(), updatedEmployee.getPassword(), Roles.ROLE_EMPLOYEE));
        updatedUser.setId(user.getId());
        updatedEmployee.setUser(updatedUser);
        return new EmployeeResponseDTO(employeeRepository.save(updatedEmployee));

    }

    public void deleteEmployee(Long employeeId) {
        var employee = existsEmployeeById(employeeId);
        employeeRepository.delete(employee);
    }

    public EmployeeResponseDTO getEmployeeById(Long employeeId) {
        return new EmployeeResponseDTO(existsEmployeeById(employeeId));
    }

    public List<EmployeeResponseDTO> topTenSallerEmployee(Company company) {
        Long companyId = company.getId();

        return employeeRepository.findTop10ByTotalSales(companyId)
                .stream()
                .map(EmployeeResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Employee existsEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException("Employee not found"));
    }

}
