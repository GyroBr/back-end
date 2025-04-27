package com.Gyro.back_end_gyro.domain.product.service;

import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductScheduleService {

    private final ProductRepository productRepository;

    @Scheduled(fixedRate = 30000)
    public void setExpiredProduct() {

        List<Product> products = productRepository.findAll();

        for (Product product : products) {

            if(product.getExpiresAt().equals(LocalDate.now())) {
                product.setIsExpiredProduct(true);
            }
        }

    }

    @Scheduled(fixedRate = 30000)
    public void setOutOfStockProduct() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            if(product.getQuantity() <= product.getWarningQuantity()){
                product.setIsOutOfStock(true);
            }
        }
    }
}
