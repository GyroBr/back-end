package com.Gyro.back_end_gyro.domain.product.service;

import com.Gyro.back_end_gyro.domain.company.entity.Company;
import com.Gyro.back_end_gyro.domain.image.service.ImageService;
import com.Gyro.back_end_gyro.domain.product.dto.ProductRequestDTO;
import com.Gyro.back_end_gyro.domain.product.dto.ProductResponseDTO;
import com.Gyro.back_end_gyro.domain.product.entity.Product;
import com.Gyro.back_end_gyro.domain.product.repository.ProductRepository;
import com.Gyro.back_end_gyro.infra.excption.handlers.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageService imageService;

    public ProductResponseDTO createProduct(Company company, ProductRequestDTO productRequestDTO, MultipartFile imageFile) {
        String imageUrl = imageService.saveImage(imageFile);
        Product product = new Product(productRequestDTO);
        product.setCompany(company);
        product.setImage(imageUrl);

        return new ProductResponseDTO(productRepository.save(product));
    }

    public List<ProductResponseDTO> getAllProductsByCompanyId(Company company) {
        return company.getProducts().stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getTop10MostSellingProducts(Long companyId) {
        return productRepository.findTopSellingProducts(companyId)
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getExiperedProducts(Long companyId) {
        return productRepository.findByCompanyIdAndIsExpiredProductTrue(companyId)
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<String> getAllProductCategories(Company company){
        return company.getProducts().stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getOutOfStockProducts(Long companyId) {
        return productRepository.findByCompanyIdAndIsExpiredProductTrue(companyId)
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProductById(Long id) {
        return new ProductResponseDTO(existsProductById(id));
    }

    public Product existsProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }
}
