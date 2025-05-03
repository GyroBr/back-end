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
import org.springframework.beans.factory.annotation.Value;
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
                            "https://www.google.com/imgres?q=Coca-cola%20foto&imgurl=https%3A%2F%2Fbrf.file.force.com%2Fservlet%2Fservlet.ImageServer%3Fid%3D015U600000027Gj%26oid%3D00D410000012TJa%26lastMod%3D1703778094000&imgrefurl=https%3A%2F%2Fwww.mercatoemcasa.com.br%2Fproduto%2Fcoca-cola-2l&docid=I_NxyW9pdPlIqM&tbnid=Rk1RDlBy-xC9IM&vet=12ahUKEwjfiZiGmJKMAxWIppUCHTLTEvsQM3oECGQQAA..i&w=800&h=800&hcb=2&ved=2ahUKEwjfiZiGmJKMAxWIppUCHTLTEvsQM3oECGQQAA",
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
                            "https://www.google.com/imgres?q=Fanta%20Laranja%20foto&imgurl=https%3A%2F%2Fwww.clubeextra.com.br%2Fimg%2Fuploads%2F1%2F177%2F25294177.png&imgrefurl=https%3A%2F%2Fwww.clubeextra.com.br%2Fproduto%2F12767%2Frefrigerante-laranja-fanta-garrafa-2l&docid=jwa1ig8pyAL8pM&tbnid=3CtWu6JOroOHXM&vet=12ahUKEwjRzPSPmJKMAxURjpUCHW9fKPgQM3oECFwQAA..i&w=1200&h=1200&hcb=2&ved=2ahUKEwjRzPSPmJKMAxURjpUCHW9fKPgQM3oECFwQAA",
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
                            "https://www.google.com/imgres?q=Sprite%20foto&imgurl=https%3A%2F%2Ffarmaciasnissei.com.br%2Fmedia%2Fprodutos%2F17863_400x400_XuGgRTi.webp&imgrefurl=https%3A%2F%2Fwww.farmaciasnissei.com.br%2Frefrigerante-sprite-sem-acucar-2-litros&docid=0jVWlz9nkFgqnM&tbnid=NFG13EG-12oWWM&vet=12ahUKEwjZkYabmJKMAxUrr5UCHcT4E1oQM3oECC4QAA..i&w=400&h=400&hcb=2&ved=2ahUKEwjZkYabmJKMAxUrr5UCHcT4E1oQM3oECC4QAA",
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
                            "https://www.google.com/imgres?q=foto%20guaran%C3%A1%20antarctica&imgurl=https%3A%2F%2Fwww.davo.com.br%2Fccstore%2Fv1%2Fimages%2F%3Fsource%3D%2Ffile%2Fv5520072052001468059%2Fproducts%2Fprod_7891991001373.imagem1.jpg%26height%3D940%26width%3D940&imgrefurl=https%3A%2F%2Fwww.davo.com.br%2Fbebidas%2Frefrigerante-guaran%25C3%25A1-antarctica-sem-a%25C3%25A7%25C3%25BAcar-2l-pet%2Fprod_7891991001373&docid=k4p7-CS6u0F2iM&tbnid=fXLkTkL4uWNjpM&vet=12ahUKEwiHle6ll5KMAxV6r5UCHaetJeoQM3oECBkQAA..i&w=768&h=940&hcb=2&ved=2ahUKEwiHle6ll5KMAxV6r5UCHaetJeoQM3oECBkQAA",
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
                            "https://www.google.com/imgres?q=Schweppes%20T%C3%B4nica%20foto&imgurl=http%3A%2F%2Facdn-us.mitiendanube.com%2Fstores%2F930%2F308%2Fproducts%2Fschweppes-250-ml-vidro1-3d117e103ce99c3a3d16039210534381-640-0.jpg&imgrefurl=https%3A%2F%2Fwww.gracinda.com.br%2Fprodutos%2Ftonica-schweppes-vidro-250-ml%2F&docid=BIKbZNj7P8t51M&tbnid=353WfSpqKOMKaM&vet=12ahUKEwiqiqenmJKMAxVJrJUCHZMUOUEQM3oECFYQAA..i&w=209&h=241&hcb=2&ved=2ahUKEwiqiqenmJKMAxVJrJUCHZMUOUEQM3oECFYQAA",
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
                            "https://www.google.com/imgres?q=%C3%81gua%20Mineral%20Nestl%C3%A9%20foto&imgurl=https%3A%2F%2Fwww.nestlepurezavital.com.br%2Fsites%2Fg%2Ffiles%2Fxknfdk1096%2Ffiles%2F18_NPV_550x750.png&imgrefurl=https%3A%2F%2Fwww.nestlepurezavital.com.br%2F1.5l-sem-gas&docid=MkGlKmkScKGP2M&tbnid=QGFkD2bcYfgiSM&vet=12ahUKEwjTnJm0mJKMAxW5pZUCHT5ZN84QM3oECGgQAA..i&w=550&h=750&hcb=2&ved=2ahUKEwjTnJm0mJKMAxW5pZUCHT5ZN84QM3oECGgQAA",
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
                            "https://www.google.com/imgres?q=Kuat%20foto&imgurl=https%3A%2F%2Fhiperideal.vtexassets.com%2Farquivos%2Fids%2F213351%2F1503405.png%3Fv%3D638278105562570000&imgrefurl=https%3A%2F%2Fwww.hiperideal.com.br%2Frefrigerante-guarana-kuat-1l%2Fp&docid=B9CENxIfEHiAgM&tbnid=G_wDelqML8tswM&vet=12ahUKEwixrsC-mJKMAxWljJUCHZnXMkoQM3oECB4QAA..i&w=1000&h=1000&hcb=2&ved=2ahUKEwixrsC-mJKMAxWljJUCHZnXMkoQM3oECB4QAA",
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
                            "https://www.google.com/imgres?q=Pepsi%20foto&imgurl=https%3A%2F%2Fsuperprix.vteximg.com.br%2Farquivos%2Fids%2F203553-600-600%2Fd416af9646f567ce75a1006891b73cdf_refrigerante-pepsi-2l_lett_1.jpg%3Fv%3D637695819528930000&imgrefurl=https%3A%2F%2Fwww.superprix.com.br%2Frefrigerante-pepsi-2l%2Fp&docid=uBPixkacHjwDdM&tbnid=YahMGHWcHJRFuM&vet=12ahUKEwjhrsnMmJKMAxVMupUCHbfPJnQQM3oECGMQAA..i&w=600&h=600&hcb=2&ved=2ahUKEwjhrsnMmJKMAxVMupUCHbfPJnQQM3oECGMQAA",
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
                            "https://www.google.com/imgres?q=Red%20Bull%20foto&imgurl=https%3A%2F%2Fsuperprix.vteximg.com.br%2Farquivos%2Fids%2F175259%2FBebida-Energetica-Red-Bull-250ml.png%3Fv%3D636299404178930000&imgrefurl=https%3A%2F%2Fwww.superprix.com.br%2Fbebida-energetica-red-bull-250ml%2Fp&docid=VRUlCdE3gIAgqM&tbnid=hEtsDHadGOyHNM&vet=12ahUKEwiNg87UmJKMAxWRkZUCHeHMLEAQM3oECBUQAA..i&w=600&h=600&hcb=2&ved=2ahUKEwiNg87UmJKMAxWRkZUCHeHMLEAQM3oECBUQAA",
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
                            "https://www.monsterenergy.com.br/assets/images/product_monster_energy.jpg",
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
                            "https://www.lipton.com.br/assets/images/product_lipton_cha_preto.jpg",
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
                            "https://www.coca-cola.com.br/pt-br/images/product_coca_cola_zero.webp",
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
                            "https://www.h2oh.com.br/assets/images/product_h2oh.jpg",
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
                            "https://www.google.com/imgres?q=Tetra%20Pak%20Suco%20de%20Laranja%20foto&imgurl=https%3A%2F%2Fimg.megaboxatacado.com.br%2Fproduto%2F1000X1000%2F2012620_35903.jpg&imgrefurl=https%3A%2F%2Fwww.megaboxatacado.com.br%2Fsuco-pronto-organico-native-laranja-tetra-pak-200-ml%2F35903%2Fp&docid=KFSLMjMJq9o6tM&tbnid=T5mUHMkpbcd-aM&vet=12ahUKEwi9sajCl5KMAxVeppUCHWLvKBcQM3oFCIMBEAA..i&w=430&h=430&hcb=2&ved=2ahUKEwi9sajCl5KMAxVeppUCHWLvKBcQM3oFCIMBEAA",
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
                            "https://www.cha-mate-leao.com.br/assets/images/product_cha_mate_leao.jpg",
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
                            "https://www.coca-cola.com.br/pt-br/images/product_coca_cola_diet.jpg",
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
                            "https://www.guarana.com.br/images/product_guarana_jesus.jpg",
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
                            "https://www.h2oh.com.br/assets/images/product_h2oh_limao.jpg",
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
                            "https://www.schweppes.com.br/assets/images/product_schweppes_citrus.jpg",
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
                            "https://www.pepsi.com.br/assets/images/product_pepsi_black.jpg",
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
                            "https://www.skol.com.br/images/product_skol.jpg",
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
                            "https://www.brahma.com.br/images/product_brahma.jpg",
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
                            "https://www.heineken.com.br/images/product_heineken.jpg",
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
                            "https://www.sucodelaranja.com.br/images/product_suco_laranja.jpg",
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
                            "https://www.sucodeuva.com.br/images/product_suco_uva.jpg",
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
                            "https://www.sucomaracuja.com.br/images/product_suco_maracuja.jpg",
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
                            "https://www.sucomanga.com.br/images/product_suco_manga.jpg",
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
                            "https://www.sucoabacaxi.com.br/images/product_suco_abacaxi.jpg",
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




