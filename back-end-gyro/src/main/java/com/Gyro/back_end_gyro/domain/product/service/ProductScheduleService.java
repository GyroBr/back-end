package com.Gyro.back_end_gyro.domain.product.service;

import com.Gyro.back_end_gyro.domain.email.service.EmailService;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductScheduleService {

    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 30000)
    public void handleExpiredProducts() {
        log.info("[Produtos Vencidos] Iniciando verificação...");

        try {
            // 1. Encontra produtos que venceram mas não foram marcados como expirados
            List<Product> newlyExpired = productRepository
                    .findByExpiresAtBeforeAndIsExpiredProductFalse(LocalDate.now());

            // 2. Marca como expirados
            newlyExpired.forEach(p -> {
                p.setIsExpiredProduct(true);
                p.setIsSendedToEmail(false);
            });
            productRepository.saveAll(newlyExpired);

            // 3. Busca produtos expirados não notificados
            List<Product> expiredToNotify = productRepository
                    .findByIsExpiredProductTrueAndIsSendedToEmailFalse();

            // 4. Agrupa por empresa e envia emails
            Map<String, List<Product>> expiredByEmail = expiredToNotify.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getCompany().getEmail()
                    ));

            expiredByEmail.forEach((email, products) -> {
                try {
                    emailService.sendEmail(
                            email,
                            "Produtos vencidos - Ação necessária",
                            buildExpiredProductsMessage(products)
                    );

                    products.forEach(p -> p.setIsSendedToEmail(true));
                    productRepository.saveAll(products);

                    log.info("Notificação enviada para {}: {} produtos", email, products.size());
                } catch (Exception e) {
                    log.error("Falha ao enviar email para {}: {}", email, e.getMessage());
                }
            });

        } catch (Exception e) {
            log.error("Erro na verificação de produtos vencidos: {}", e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 30000)
    public void handleOutOfStockProducts() {
        log.info("[Estoque Baixo] Iniciando verificação...");

        try {
            // 1. Encontra o menor warningQuantity
            Integer minWarning = productRepository.findAll()
                    .stream()
                    .mapToInt(Product::getWarningQuantity)
                    .min()
                    .orElse(0);

            List<Product> newlyOutOfStock = productRepository
                    .findByQuantityLessThanEqualAndIsOutOfStockFalse(minWarning);

            newlyOutOfStock.forEach(p -> {
                p.setIsOutOfStock(true);
                p.setIsSendedToEmail(false);
            });
            productRepository.saveAll(newlyOutOfStock);

            List<Product> outOfStockToNotify = productRepository
                    .findByIsOutOfStockTrueAndIsSendedToEmailFalse();

            Map<String, List<Product>> outOfStockByEmail = outOfStockToNotify.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getCompany().getEmail()
                    ));

            outOfStockByEmail.forEach((email, products) -> {
                try {
                    emailService.sendEmail(
                            email,
                            "Produtos com estoque baixo - Ação necessária",
                            buildOutOfStockMessage(products)
                    );

                    products.forEach(p -> p.setIsSendedToEmail(true));
                    productRepository.saveAll(products);

                    log.info("Notificação enviada para {}: {} produtos", email, products.size());
                } catch (Exception e) {
                    log.error("Falha ao enviar email para {}: {}", email, e.getMessage());
                }
            });

        } catch (Exception e) {
            log.error("Erro na verificação de estoque baixo: {}", e.getMessage(), e);
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