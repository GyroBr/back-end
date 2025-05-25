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

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageService imageService;

    public ProductResponseDTO createProduct(Company company, ProductRequestDTO request, MultipartFile imageFile) {
        String imageUrl = imageService.saveImage(imageFile);
        Product product = buildProductFromRequest(request, company, imageUrl);
        return new ProductResponseDTO(productRepository.save(product));
    }

    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO request) {
        Product existingProduct = getProductByIdOrThrow(productId);
        Product updatedProduct = updateProductFromRequest(existingProduct, request);
        return new ProductResponseDTO(productRepository.save(updatedProduct));
    }

    public void deleteProduct(Long productId) {
        productRepository.delete(getProductByIdOrThrow(productId));
    }

    public List<ProductResponseDTO> getAllProductsByCompany(Company company) {
        return company.getProducts().stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public List<ProductResponseDTO> getTopSellingProducts(Long companyId) {
        return productRepository.findTopSellingProducts(companyId).stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public List<ProductResponseDTO> getExpiredProducts(Long companyId) {
        return productRepository.findByCompanyIdAndIsExpiredProductTrue(companyId).stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public List<String> getProductCategories(Company company) {
        return company.getProducts().stream()
                .map(Product::getCategory)
                .distinct()
                .toList();
    }

    public List<ProductResponseDTO> getOutOfStockProducts(Long companyId) {
        return productRepository.findByCompanyIdAndIsOutOfStockTrue(companyId).stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public ProductResponseDTO getProductResponseById(Long id) {
        return new ProductResponseDTO(getProductByIdOrThrow(id));
    }

    public Product getProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }

    public Product existsProductAndCompany(Long id, Company company) {
        return productRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado ou não pertence à empresa"));
    }

    private Product buildProductFromRequest(ProductRequestDTO request, Company company, String imageUrl) {
        Product product = new Product(request);
        product.setCompany(company);
        product.setImage(imageUrl);
        return product;
    }

    private Product updateProductFromRequest(Product existingProduct, ProductRequestDTO request) {
        Product updatedProduct = new Product(request);
        updatedProduct.setId(existingProduct.getId());
        updatedProduct.setCompany(existingProduct.getCompany());
        updatedProduct.setTotalSales(existingProduct.getTotalSales());
        updatedProduct.setComboProducts(existingProduct.getComboProducts());
        updatedProduct.setOrderProducts(existingProduct.getOrderProducts());
        return updatedProduct;
    }
}