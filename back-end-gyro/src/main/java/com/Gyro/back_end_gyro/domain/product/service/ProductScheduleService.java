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

    @Scheduled(fixedRate = 60000)
    public void handleExpiredProducts() {
        List<Product> allProducts = productRepository.findAll();
        Map<String, List<Product>> productsByEmail = new HashMap<>();

        for (Product product : allProducts) {
            if (LocalDate.now().isAfter(product.getExpiresAt()) && !product.getIsExpiredProduct()) {
                product.setIsExpiredProduct(true);
                product.setIsSendedToEmail(false);
                productRepository.save(product);

                String email = product.getCompany().getEmail();
                if (!productsByEmail.containsKey(email)) {
                    productsByEmail.put(email, new ArrayList<>());
                }
                productsByEmail.get(email).add(product);
            }
        }

        for (Map.Entry<String, List<Product>> entry : productsByEmail.entrySet()) {
            emailService.sendEmail(
                    entry.getKey(),
                    "Produtos vencidos",
                    buildExpiredProductsMessage(entry.getValue())
            );

            for (Product product : entry.getValue()) {
                product.setIsSendedToEmail(true);
                productRepository.save(product);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void handleOutOfStockProducts() {
        List<Product> allProducts = productRepository.findAll();
        Map<String, List<Product>> productsByEmail = new HashMap<>();

        int minWarning = Integer.MAX_VALUE;
        for (Product product : allProducts) {
            if (product.getWarningQuantity() < minWarning) {
                minWarning = product.getWarningQuantity();
            }
        }

        for (Product product : allProducts) {
            if (product.getQuantity() <= minWarning && !product.getIsSendedToEmail()) {
                product.setIsOutOfStock(true);
                product.setIsSendedToEmail(true);
                productRepository.save(product);

                String email = product.getCompany().getEmail();
                if (!productsByEmail.containsKey(email)) {
                    productsByEmail.put(email, new ArrayList<>());
                }
                productsByEmail.get(email).add(product);
            }
        }

        for (Map.Entry<String, List<Product>> entry : productsByEmail.entrySet()) {
            emailService.sendEmail(
                    entry.getKey(),
                    "Produtos com estoque baixo",
                    buildOutOfStockMessage(entry.getValue())
            );
        }
    }

    private String buildExpiredProductsMessage(List<Product> products) {
        StringBuilder sb = new StringBuilder("Produtos vencidos:\n\n");
        for (Product product : products) {
            sb.append("- ")
                    .append(product.getName())
                    .append(" (Venceu em: ")
                    .append(product.getExpiresAt())
                    .append(")\n");
        }
        return sb.toString();
    }

    private String buildOutOfStockMessage(List<Product> products) {
        StringBuilder sb = new StringBuilder("Produtos abaixo do estoque mínimo:\n\n");
        for (Product product : products) {
            sb.append("- ")
                    .append(product.getName())
                    .append(" (Estoque atual: ")
                    .append(product.getQuantity())
                    .append(", Mínimo: ")
                    .append(product.getWarningQuantity())
                    .append(")\n");
        }
        return sb.toString();
    }
}