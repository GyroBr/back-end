package com.Gyro.back_end_gyro.infra.config;

import com.Gyro.back_end_gyro.domain.address.dto.AddressRequestDTO;
import com.Gyro.back_end_gyro.domain.address.entity.Address;
import com.Gyro.back_end_gyro.domain.company.dto.CompanyRequestDTO;
import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.company.enums.Sector;
import com.Gyro.back_end_gyro.domain.company.repository.CompanyRepository;
import com.Gyro.back_end_gyro.domain.employee.dto.EmployeeRequestDTO;
import com.Gyro.back_end_gyro.domain.employee.entity.Employee;
import com.Gyro.back_end_gyro.domain.employee.repository.EmployeeRepository;
import com.Gyro.back_end_gyro.domain.product.dto.ProductRequestDTO;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.repository.ProductRepository;
import com.Gyro.back_end_gyro.domain.user.dto.UserRequestDTO;
import com.Gyro.back_end_gyro.domain.user.entity.User;
import com.Gyro.back_end_gyro.domain.user.enums.Roles;
import com.Gyro.back_end_gyro.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;

    private Company toSeed(CompanyRequestDTO companyRequestDTO) {
        User user = userService.createUser(new UserRequestDTO(companyRequestDTO.name(), companyRequestDTO.email(), companyRequestDTO.password(), Roles.ROLE_ADMIN));
        Address address = new Address(companyRequestDTO.address());
        Company company = new Company(companyRequestDTO);
        company.setPassword(passwordEncoder.encode(companyRequestDTO.password()));
        company.setUser(user);
        company.setAddress(address);
        return companyRepository.save(company);
    }


    @Override
    public void run(String... args) throws Exception {


        if (companyRepository.findAll().isEmpty()) {
            System.out.println(" _ __   ___  _ __  _   _| | __ _ _ __   __| | ___     ___  \n" +
                    "| '_ \\ / _ \\| '_ \\| | | | |/ _` | '_ \\ / _` |/ _ \\   / _ \\ \n" +
                    "| |_) | (_) | |_) | |_| | | (_| | | | | (_| | (_) | | (_) |\n" +
                    "| .__/ \\___/| .__/ \\__,_|_|\\__,_|_| |_|\\__,_|\\___/   \\___/ \n" +
                    "|_|         |_|                                            \n" +
                    " _                           \n" +
                    "| |__   __ _ _ __   ___ ___  \n" +
                    "| '_ \\ / _` | '_ \\ / __/ _ \\ \n" +
                    "| |_) | (_| | | | | (_| (_) |\n" +
                    "|_.__/ \\__,_|_| |_|\\___\\___/ \n");


            System.out.println(" ___ _ __ ___  \n" +
                    " / _ \\ '_ ` _ \\ \n" +
                    "|  __/ | | | | |\n" +
                    " \\___|_| |_| |_|\n" +
                    "                \n");


            System.out.println(" _____ \n" +
                    "|___ / \n" +
                    "  |_ \\ \n" +
                    " ___) |\n" +
                    "|____/");


            System.out.println("____  \n" +
                    "|___ \\ \n" +
                    "  __) |\n" +
                    " / __/ \n" +
                    "|_____|\n");


            System.out.println(" _ \n" +
                    "/ |\n" +
                    "| |\n" +
                    "| |\n" +
                    "|_|\n");


            System.out.println("(_) (_) (_)\n");


            Company company = toSeed(new CompanyRequestDTO(
                    "Gyro",
                    "11951982246",
                    "35107893000153",
                    "gyro@gyro.com",
                    "123123",
                    Sector.FOOD_TRUCKS,
                    new AddressRequestDTO(
                            "01310904",
                            "Bela vista",
                            "Paulista",
                            "SP",
                            "São Paulo",
                            "3122")
            ));


            Employee employee1 = new Employee(
                    new EmployeeRequestDTO(
                            "Davi Francisco",
                            "davi@gyro.com",
                            "123123")
            );

            User user1 = userService.createUser(
                    new UserRequestDTO(
                            employee1.getName(),
                            employee1.getEmail(),
                            employee1.getPassword(),
                            Roles.ROLE_EMPLOYEE)
            );

            employee1.setUser(user1);
            employee1.setCompany(company);
            employeeRepository.save(employee1);


            Employee employee2 = new Employee(
                    new EmployeeRequestDTO(
                            "Eduardo Melo de Oliveira",
                            "eduardo@gyro.com",
                            "123123")
            );

            User user2 = userService.createUser(
                    new UserRequestDTO(

                            employee2.getName(),
                            employee2.getEmail(),
                            employee2.getPassword(),
                            Roles.ROLE_EMPLOYEE));

            employee2.setUser(user2);
            employee2.setCompany(company);
            employeeRepository.save(employee2);


            Employee employee3 = new Employee(

                    new EmployeeRequestDTO(
                            "Gustavo Viana Pardinho",
                            "gustavo@gyro.com",
                            "123123")
            );

            User user3 = userService.createUser(

                    new UserRequestDTO(
                            employee3.getName(),
                            employee3.getEmail(),
                            employee3.getPassword(),
                            Roles.ROLE_EMPLOYEE
                    )
            );

            employee3.setUser(user3);
            employee3.setCompany(company);
            employeeRepository.save(employee3);


            Employee employee4 = new Employee(
                    new EmployeeRequestDTO(
                            "Beatriz",
                            "beatriz@gyro.com",
                            "123123"
                    )
            );

            User user4 = userService.createUser(

                    new UserRequestDTO(
                            employee4.getName(),
                            employee4.getEmail(),
                            employee4.getPassword(),
                            Roles.ROLE_EMPLOYEE
                    )
            );

            employee4.setUser(user4);
            employee4.setCompany(company);
            employeeRepository.save(employee4);


            Employee employee5 = new Employee(

                    new EmployeeRequestDTO(
                            "Michelly Mendes",
                            "michelly@gyro.com",
                            "123123"
                    )
            );

            User user5 = userService.createUser(

                    new UserRequestDTO(
                            employee5.getName(),
                            employee5.getEmail(),
                            employee5.getPassword(),
                            Roles.ROLE_EMPLOYEE
                    )
            );

            employee5.setUser(user5);
            employee5.setCompany(company);
            employeeRepository.save(employee5);


            Employee employee6 = new Employee(
                    new EmployeeRequestDTO(
                            "Felipe Magalhães",
                            "felipe@gyro.com",
                            "123123"
                    )
            );

            User user6 = userService.createUser(
                    new UserRequestDTO(
                            employee6.getName(),
                            employee6.getEmail(),
                            employee6.getPassword(),
                            Roles.ROLE_EMPLOYEE
                    )
            );

            employee6.setUser(user6);
            employee6.setCompany(company);
            employeeRepository.save(employee6);


            Product product1 = new Product(
                    new ProductRequestDTO(
                            "Coca-cola",
                            4.19,
                            "350ml",
                            100,
                            LocalDate.now().plusDays(20),
                            40,
                            "refrigerante",
                            "no-one "
                    )
            );
            product1.setCompany(company);
            productRepository.save(product1);

            Product product2 = new Product(
                    new ProductRequestDTO(
                            "Fanta Laranja",
                            5.99,
                            "350ml",
                            120,
                            LocalDate.now().plusDays(32),
                            55,
                            "refrigerante",
                            "no-one"
                    )
            );
            product2.setCompany(company);
            productRepository.save(product2);

            Product product3 = new Product(
                    new ProductRequestDTO(
                            "Sprite",
                            5.49,
                            "350ml",
                            130,
                            LocalDate.now().plusDays(35),
                            60,
                            "refrigerante",
                            "no-one"
                    )
            );
            product3.setCompany(company);
            productRepository.save(product3);

            Product product4 = new Product(
                    new ProductRequestDTO(
                            "Guaraná Antarctica",
                            6.19,
                            "350ml",
                            200,
                            LocalDate.now().plusDays(28),
                            65,
                            "refrigerante",
                            "no-one"
                    )
            );
            product4.setCompany(company);
            productRepository.save(product4);

            Product product5 = new Product(
                    new ProductRequestDTO(
                            "Schweppes Tônica",
                            7.99,
                            "350ml",
                            110,
                            LocalDate.now().plusDays(40),
                            70,
                            "refrigerante",
                            "no-one"
                    )
            );
            product5.setCompany(company);
            productRepository.save(product5);

            Product product6 = new Product(
                    new ProductRequestDTO(
                            "Água Mineral Nestlé",
                            3.99,
                            "500ml",
                            300,
                            LocalDate.now().plusDays(15),
                            80,
                            "água",
                            "no-one"
                    )
            );
            product6.setCompany(company);
            productRepository.save(product6);

            Product product7 = new Product(
                    new ProductRequestDTO(
                            "Kuat",
                            6.59,
                            "350ml",
                            130,
                            LocalDate.now().plusDays(37),
                            85,
                            "refrigerante",
                            "no-one"
                    )
            );
            product7.setCompany(company);
            productRepository.save(product7);

            Product product8 = new Product(
                    new ProductRequestDTO(
                            "Pepsi",
                            5.79,
                            "350ml",
                            180,
                            LocalDate.now().plusDays(50),
                            90,
                            "refrigerante",
                            "no-one"
                    )
            );
            product8.setCompany(company);
            productRepository.save(product8);

            Product product9 = new Product(
                    new ProductRequestDTO(
                            "Red Bull",
                            10.00,
                            "250ml",
                            90,
                            LocalDate.now().plusDays(60),
                            100,
                            "energéticos",
                            "no-one"
                    )
            );
            product9.setCompany(company);
            productRepository.save(product9);

            Product product10 = new Product(
                    new ProductRequestDTO(
                            "Monster Energy",
                            12.49,
                            "500ml",
                            200,
                            LocalDate.now().plusDays(70),
                            110,
                            "energéticos",
                            "no-one"
                    )
            );
            product10.setCompany(company);
            productRepository.save(product10);

            Product product11 = new Product(
                    new ProductRequestDTO(
                            "Lipton Chá Preto",
                            5.99,
                            "350ml",
                            100,
                            LocalDate.now().plusDays(20),
                            120,
                            "chá",
                            "no-one"
                    )
            );
            product11.setCompany(company);
            productRepository.save(product11);

            Product product12 = new Product(
                    new ProductRequestDTO(
                            "Coca-Cola Zero",
                            7.49,
                            "350ml",
                            150,
                            LocalDate.now().plusDays(45),
                            130,
                            "refrigerante",
                            "no-one"
                    )
            );
            product12.setCompany(company);
            productRepository.save(product12);

            Product product13 = new Product(
                    new ProductRequestDTO(
                            "H2OH!",
                            5.19,
                            "500ml",
                            120,
                            LocalDate.now().plusDays(25),
                            140,
                            "água",
                            "no-one"
                    )
            );
            product13.setCompany(company);
            productRepository.save(product13);

            Product product14 = new Product(
                    new ProductRequestDTO(
                            "Tetra Pak Suco de Laranja",
                            4.99,
                            "1L",
                            200,
                            LocalDate.now().plusDays(55),
                            150,
                            "suco",
                            "no-one"
                    )
            );
            product14.setCompany(company);
            productRepository.save(product14);

            Product product15 = new Product(
                    new ProductRequestDTO(
                            "Chá Mate Leão",
                            4.49,
                            "350ml",
                            110,
                            LocalDate.now().plusDays(60),
                            160,
                            "chá",
                            "no-one"
                    )
            );
            product15.setCompany(company);
            productRepository.save(product15);

            Product product16 = new Product(
                    new ProductRequestDTO(
                            "Coca-Cola Diet",
                            6.99,
                            "350ml",
                            130,
                            LocalDate.now().plusDays(75),
                            170,
                            "refrigerante",
                            "no-one"
                    )
            );
            product16.setCompany(company);
            productRepository.save(product16);

            Product product17 = new Product(
                    new ProductRequestDTO(
                            "Guaraná Jesus",
                            7.19,
                            "350ml",
                            140,
                            LocalDate.now().plusDays(80),
                            180,
                            "refrigerante",
                            "no-one"
                    )
            );
            product17.setCompany(company);
            productRepository.save(product17);

            Product product18 = new Product(
                    new ProductRequestDTO(
                            "H2OH! Limão",
                            5.99,
                            "500ml",
                            200,
                            LocalDate.now().plusDays(85),
                            190,
                            "água",
                            "no-one"
                    )
            );
            product18.setCompany(company);
            productRepository.save(product18);

            Product product19 = new Product(
                    new ProductRequestDTO(
                            "Schweppes Citrus",
                            7.39,
                            "350ml",
                            210,
                            LocalDate.now().plusDays(90),
                            200,
                            "refrigerante",
                            "no-one"
                    )
            );
            product19.setCompany(company);
            productRepository.save(product19);

            Product product20 = new Product(
                    new ProductRequestDTO(
                            "Pepsi Black",
                            6.89,
                            "350ml",
                            180,
                            LocalDate.now().plusDays(100),
                            210,
                            "refrigerante",
                            "no-one"
                    )
            );
            product20.setCompany(company);
            productRepository.save(product20);

            Product product21 = new Product(
                    new ProductRequestDTO(
                            "Skol",
                            5.99,
                            "350ml",
                            150,
                            LocalDate.now().plusDays(60),
                            50,
                            "alcoólicos",
                            "no-one"
                    )
            );
            product21.setCompany(company);
            productRepository.save(product21);

            Product product22 = new Product(
                    new ProductRequestDTO(
                            "Brahma",
                            6.49,
                            "350ml",
                            140,
                            LocalDate.now().plusDays(65),
                            55,
                            "alcoólicos",
                            "no-one"
                    )
            );
            product22.setCompany(company);
            productRepository.save(product22);

            Product product23 = new Product(
                    new ProductRequestDTO(
                            "Heineken",
                            8.99,
                            "350ml",
                            120,
                            LocalDate.now().plusDays(70),
                            60,
                            "alcoólicos",
                            "no-one"
                    )
            );
            product23.setCompany(company);
            productRepository.save(product23);

            Product product24 = new Product(
                    new ProductRequestDTO(
                            "Suco de Laranja Natural",
                            7.99,
                            "1L",
                            100,
                            LocalDate.now().plusDays(10),
                            30,
                            "suco",
                            "no-one"
                    )
            );
            product24.setCompany(company);
            productRepository.save(product24);

            Product product25 = new Product(
                    new ProductRequestDTO(
                            "Suco de Uva Integral",
                            8.49,
                            "1L",
                            90,
                            LocalDate.now().plusDays(15),
                            35,
                            "suco",
                            "no-one"
                    )
            );
            product25.setCompany(company);
            productRepository.save(product25);

            Product product26 = new Product(
                    new ProductRequestDTO(
                            "Suco de Maracujá",
                            6.99,
                            "1L",
                            110,
                            LocalDate.now().plusDays(12),
                            40,
                            "suco",
                            "no-one"
                    )
            );
            product26.setCompany(company);
            productRepository.save(product26);

            Product product27 = new Product(
                    new ProductRequestDTO(
                            "Suco de Manga",
                            7.49,
                            "1L",
                            95,
                            LocalDate.now().plusDays(18),
                            45,
                            "suco",
                            "no-one"
                    )
            );
            product27.setCompany(company);
            productRepository.save(product27);

            Product product28 = new Product(
                    new ProductRequestDTO(
                            "Suco de Abacaxi com Hortelã",
                            8.99,
                            "1L",
                            85,
                            LocalDate.now().plusDays(20),
                            50,
                            "suco",
                            "no-one"
                    )
            );
            product28.setCompany(company);
            productRepository.save(product28);

            System.out.println("| __ )  __ _ _ __   ___ ___    _ __   ___  _ __  _   _| | __ _  __| | ___  \n" +
                    "|  _ \\ / _` | '_ \\ / __/ _ \\  | '_ \\ / _ \\| '_ \\| | | | |/ _` |/ _` |/ _ \\ \n" +
                    "| |_) | (_| | | | | (_| (_) | | |_) | (_) | |_) | |_| | | (_| | (_| | (_) |\n" +
                    "|____/ \\__,_|_| |_|\\___\\___/  | .__/ \\___/| .__/ \\__,_|_|\\__,_|\\__,_|\\___/ \n" +
                    "                              |_|         |_|                              \n" +
                    "                                                         \n" +
                    "  ___ ___  _ __ ___    ___ _   _  ___ ___  ___ ___  ___  \n" +
                    " / __/ _ \\| '_ ` _ \\  / __| | | |/ __/ _ \\/ __/ __|/ _ \\ \n" +
                    "| (_| (_) | | | | | | \\__ \\ |_| | (_|  __/\\__ \\__ \\ (_) |\n" +
                    " \\___\\___/|_| |_| |_| |___/\\__,_|\\___\\___||___/___/\\___/ \n");
        }

    }
}



