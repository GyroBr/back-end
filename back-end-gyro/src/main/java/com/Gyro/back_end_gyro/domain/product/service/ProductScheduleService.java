package com.Gyro.back_end_gyro.domain.product.service;

import com.Gyro.back_end_gyro.domain.email.service.EmailService;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductScheduleService {

    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 30000)
    public void handleExpiredProducts() {
        log.info("[Produtos Vencidos] Iniciando verificação de produtos vencidos...");

        try {
            List<Product> productsToCheck = productRepository.findAll()
                    .stream()
                    .filter(p -> !p.getIsExpiredProduct() || !p.getIsSendedToEmail())
                    .toList();

            log.info("[Produtos Vencidos] {} produtos para verificar", productsToCheck.size());

            Map<String, List<Product>> expiredProductsByEmail = productsToCheck.stream()
                    .filter(p -> LocalDate.now().isAfter(p.getExpiresAt()))
                    .peek(p -> {
                        p.setIsExpiredProduct(true);
                        p.setIsSendedToEmail(false);
                        productRepository.save(p);
                    })
                    .collect(Collectors.groupingBy(
                            p -> p.getCompany().getEmail(),
                            Collectors.toList()
                    ));

            log.info("[Produtos Vencidos] {} empresas com produtos vencidos", expiredProductsByEmail.size());

            expiredProductsByEmail.forEach((email, products) -> {
                try {
                    emailService.sendEmail(
                            email,
                            "Produtos vencidos - Ação necessária",
                            buildExpiredProductsMessage(products)
                    );

                    products.forEach(p -> {
                        p.setIsSendedToEmail(true);
                        productRepository.save(p);
                    });

                    log.info("Notificação enviada para {} sobre {} produtos vencidos", email, products.size());
                } catch (Exception e) {
                    log.error("Falha ao enviar email para {}: {}", email, e.getMessage());
                }
            });

        } catch (Exception e) {
            log.error("Erro durante verificação de produtos vencidos: {}", e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 30000)
    public void handleOutOfStockProducts() {
        log.info("[Estoque Baixo] Iniciando verificação de produtos com estoque baixo...");

        try {
            List<Product> productsToCheck = productRepository.findAll()
                    .stream()
                    .filter(p -> !p.getIsOutOfStock() || !p.getIsSendedToEmail())
                    .toList();

            log.info("[Estoque Baixo] {} produtos para verificar", productsToCheck.size());

            int minWarning = productsToCheck.stream()
                    .mapToInt(Product::getWarningQuantity)
                    .min()
                    .orElse(0);

            Map<String, List<Product>> outOfStockProductsByEmail = productsToCheck.stream()
                    .filter(p -> p.getQuantity() <= minWarning)
                    .peek(p -> {
                        p.setIsOutOfStock(true);
                        p.setIsSendedToEmail(false);
                        productRepository.save(p);
                    })
                    .collect(Collectors.groupingBy(
                            p -> p.getCompany().getEmail(),
                            Collectors.toList()
                    ));

            log.info("[Estoque Baixo] {} empresas com produtos em estoque baixo", outOfStockProductsByEmail.size());

            outOfStockProductsByEmail.forEach((email, products) -> {
                try {
                    emailService.sendEmail(
                            email,
                            "Produtos com estoque baixo - Ação necessária",
                            buildOutOfStockMessage(products)
                    );

                    products.forEach(p -> {
                        p.setIsSendedToEmail(true);
                        productRepository.save(p);
                    });

                    log.info("Notificação enviada para {} sobre {} produtos com estoque baixo", email, products.size());
                } catch (Exception e) {
                    log.error("Falha ao enviar email para {}: {}", email, e.getMessage());
                }
            });

        } catch (Exception e) {
            log.error("Erro durante verificação de estoque baixo: {}", e.getMessage(), e);
        }
    }

    private String buildExpiredProductsMessage(List<Product> products) {
        StringBuilder message = new StringBuilder();
        message.append("Os seguintes produtos estão vencidos:\n\n");

        products.forEach(p -> {
            message.append(String.format(
                    "- %s (ID: %d) - Vencido em: %s\n",
                    p.getName(),
                    p.getId(),
                    p.getExpiresAt()
            ));
        });

        message.append("\nPor favor, tome as providências necessárias.\n");
        return message.toString();
    }

    private String buildOutOfStockMessage(List<Product> products) {
        StringBuilder message = new StringBuilder();
        message.append("Os seguintes produtos estão abaixo do estoque mínimo:\n\n");

        products.forEach(p -> {
            message.append(String.format(
                    "- %s (ID: %d) - Estoque: %d (Mínimo: %d)\n",
                    p.getName(),
                    p.getId(),
                    p.getQuantity(),
                    p.getWarningQuantity()
            ));
        });

        message.append("\nPor favor, realize um novo pedido para repor o estoque.\n");
        return message.toString();
    }
}