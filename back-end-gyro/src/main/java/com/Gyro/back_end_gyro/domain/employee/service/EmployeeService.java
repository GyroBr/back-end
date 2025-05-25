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
        var oldEmployee = existsEmployeeById(employeeId);
        var newEmployee = updateEmployeeFactory(oldEmployee, requestDTO);
        var updatedUserEmployee = userService.updateUser(oldEmployee, newEmployee, Roles.ROLE_EMPLOYEE);
        newEmployee.setUser(updatedUserEmployee);
        return new EmployeeResponseDTO(employeeRepository.save(newEmployee));
    }

    public void deleteEmployee(Long employeeId) {
        var employee = existsEmployeeById(employeeId);
        userService.deleteUser(employee.getUser());
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


    private Employee updateEmployeeFactory(Employee employee, EmployeeRequestDTO requestDTO) {
        var updatedEmployee = new Employee(requestDTO);
        updatedEmployee.setTotalRevenue(employee.getTotalRevenue());
        updatedEmployee.setTotalSales(employee.getTotalSales());
        updatedEmployee.setCompany(employee.getCompany());
        updatedEmployee.setId(employee.getId());
        return updatedEmployee;
    }

}
