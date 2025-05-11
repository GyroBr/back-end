package com.Gyro.back_end_gyro.domain.product.service;

import com.Gyro.back_end_gyro.domain.email.service.EmailService;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ProductScheduleService {

    private final ProductRepository productRepository;
    private final EmailService emailService;


    @Scheduled(fixedRate = 30000)
    public void setExpiredProduct() {
        List<Product> products = productRepository.findAll();
        Map<String, List<Product>> expiredProductsByEmail = new HashMap<>();

        for (Product product : products) {
            if (product.getExpiresAt().equals(LocalDate.now())) {
                product.setIsExpiredProduct(true);
                String email = product.getCompany().getEmail();
                expiredProductsByEmail
                        .computeIfAbsent(email, k -> new ArrayList<>())
                        .add(product);
            }
        }

        expiredProductsByEmail.forEach((email, expiredProducts) -> {
            emailService.sendEmail(
                    email,
                    "Produtos vencidos",
                    "Produtos vencidos: %s".formatted(expiredProducts)
            );
        });
    }

    @Scheduled(fixedRate = 30000)
    public void setOutOfStockProduct() {
        List<Product> products = productRepository.findAll();
        Map<String, List<Product>> outOfStockProductsByEmail = new HashMap<>();

        for (Product product : products) {
            if (product.getQuantity() <= product.getWarningQuantity() && !product.getIsSendedToEmail()) {
                product.setIsOutOfStock(true);
                product.setIsSendedToEmail(true);
                String email = product.getCompany().getEmail();
                outOfStockProductsByEmail
                        .computeIfAbsent(email, k -> new ArrayList<>())
                        .add(product);
            }
        }

        outOfStockProductsByEmail.forEach((email, outOfStockProducts) -> {
            emailService.sendEmail(
                    email,
                    "Produtos fora de estoque",
                    "Os seguintes produtos estão fora de estoque: %s".formatted(outOfStockProducts)
            );
        });
    }
}
