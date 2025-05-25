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
            log.info("[Produtos Vencidos] Buscando todos os produtos no banco de dados...");
            List<Product> allProducts = productRepository.findAll();
            log.info("[Produtos Vencidos] Total de produtos encontrados: {}", allProducts.size());

            Map<String, List<Product>> productsByEmail = new HashMap<>();
            int expiredProductsCount = 0;

            log.info("[Produtos Vencidos] Verificando produtos vencidos...");
            for (Product product : allProducts) {
                if (LocalDate.now().isAfter(product.getExpiresAt())) {
                    log.debug("[Produtos Vencidos] Produto encontrado com data vencida: {} (ID: {})",
                            product.getName(), product.getId());

                    if (!product.getIsExpiredProduct()) {
                        log.info("[Produtos Vencidos] Marcando produto como vencido: {} (ID: {})",
                                product.getName(), product.getId());

                        product.setIsExpiredProduct(true);
                        product.setIsSendedToEmail(false);
                        productRepository.save(product);
                        expiredProductsCount++;

                        String email = product.getCompany().getEmail();
                        productsByEmail.computeIfAbsent(email, k -> new ArrayList<>()).add(product);
                        log.debug("[Produtos Vencidos] Produto adicionado à lista de email: {}", email);
                    } else {
                        log.debug("[Produtos Vencidos] Produto já estava marcado como vencido: {} (ID: {})",
                                product.getName(), product.getId());
                    }
                }
            }

            log.info("[Produtos Vencidos] Total de produtos vencidos encontrados: {}", expiredProductsCount);
            log.info("[Produtos Vencidos] Número de emails a serem enviados: {}", productsByEmail.size());

            for (Map.Entry<String, List<Product>> entry : productsByEmail.entrySet()) {
                String email = entry.getKey();
                int productsCount = entry.getValue().size();

                log.info("[Produtos Vencidos] Preparando para enviar email para {} com {} produto(s) vencido(s)",
                        email, productsCount);

                try {
                    emailService.sendEmail(
                            email,
                            "Produtos vencidos",
                            buildExpiredProductsMessage(entry.getValue())
                    );
                    log.info("[Produtos Vencidos] Email enviado com sucesso para {}", email);

                    for (Product product : entry.getValue()) {
                        product.setIsSendedToEmail(true);
                        productRepository.save(product);
                        log.debug("[Produtos Vencidos] Produto {} (ID: {}) marcado como email enviado",
                                product.getName(), product.getId());
                    }
                } catch (Exception e) {
                    log.error("[Produtos Vencidos] Falha ao enviar email para {}: {}", email, e.getMessage(), e);
                }
            }

            log.info("[Produtos Vencidos] Processamento concluído com sucesso");
        } catch (Exception e) {
            log.error("[Produtos Vencidos] Erro durante o processamento: {}", e.getMessage(), e);
        }
    }

    @Scheduled(fixedRate = 30000)
    public void handleOutOfStockProducts() {
        log.info("[Estoque Baixo] Iniciando verificação de produtos com estoque baixo...");

        try {
            log.info("[Estoque Baixo] Buscando todos os produtos no banco de dados...");
            List<Product> allProducts = productRepository.findAll();
            log.info("[Estoque Baixo] Total de produtos encontrados: {}", allProducts.size());

            Map<String, List<Product>> productsByEmail = new HashMap<>();
            int outOfStockProductsCount = 0;

            log.info("[Estoque Baixo] Determinando quantidade mínima de alerta...");
            int minWarning = Integer.MAX_VALUE;
            for (Product product : allProducts) {
                if (product.getWarningQuantity() < minWarning) {
                    minWarning = product.getWarningQuantity();
                }
            }
            log.info("[Estoque Baixo] Quantidade mínima de alerta determinada: {}", minWarning);

            log.info("[Estoque Baixo] Verificando produtos com estoque baixo...");
            for (Product product : allProducts) {
                if (product.getQuantity() <= minWarning) {
                    log.debug("[Estoque Baixo] Produto encontrado com estoque baixo: {} (ID: {}) - Quantidade: {}, Mínimo: {}",
                            product.getName(), product.getId(), product.getQuantity(), minWarning);

                    if (!product.getIsSendedToEmail()) {
                        log.info("[Estoque Baixo] Marcando produto como estoque baixo: {} (ID: {})",
                                product.getName(), product.getId());

                        product.setIsOutOfStock(true);
                        product.setIsSendedToEmail(true);
                        productRepository.save(product);
                        outOfStockProductsCount++;

                        String email = product.getCompany().getEmail();
                        productsByEmail.computeIfAbsent(email, k -> new ArrayList<>()).add(product);
                        log.debug("[Estoque Baixo] Produto adicionado à lista de email: {}", email);
                    } else {
                        log.debug("[Estoque Baixo] Email já foi enviado para este produto: {} (ID: {})",
                                product.getName(), product.getId());
                    }
                }
            }

            log.info("[Estoque Baixo] Total de produtos com estoque baixo encontrados: {}", outOfStockProductsCount);
            log.info("[Estoque Baixo] Número de emails a serem enviados: {}", productsByEmail.size());

            for (Map.Entry<String, List<Product>> entry : productsByEmail.entrySet()) {
                String email = entry.getKey();
                int productsCount = entry.getValue().size();

                log.info("[Estoque Baixo] Preparando para enviar email para {} com {} produto(s) com estoque baixo",
                        email, productsCount);

                try {
                    emailService.sendEmail(
                            email,
                            "Produtos com estoque baixo",
                            buildOutOfStockMessage(entry.getValue())
                    );
                    log.info("[Estoque Baixo] Email enviado com sucesso para {}", email);
                } catch (Exception e) {
                    log.error("[Estoque Baixo] Falha ao enviar email para {}: {}", email, e.getMessage(), e);
                }
            }

            log.info("[Estoque Baixo] Processamento concluído com sucesso");
        } catch (Exception e) {
            log.error("[Estoque Baixo] Erro durante o processamento: {}", e.getMessage(), e);
        }
    }

    private String buildExpiredProductsMessage(List<Product> products) {
        log.debug("[Produtos Vencidos] Construindo mensagem de email para {} produtos", products.size());
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
        log.debug("[Estoque Baixo] Construindo mensagem de email para {} produtos", products.size());
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