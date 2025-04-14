package com.pranay.ecommerce.product_service.services.impl;

import com.pranay.ecommerce.product_service.Dtos.ApiResponse;
import com.pranay.ecommerce.product_service.Dtos.ProductRequest;
import com.pranay.ecommerce.product_service.Dtos.ProductResponse;
import com.pranay.ecommerce.product_service.exceptions.ProductNotFoundException;
import com.pranay.ecommerce.product_service.models.Product;
import com.pranay.ecommerce.product_service.repositories.ProductRepository;
import com.pranay.ecommerce.product_service.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductResponse createProduct(ProductRequest productRequest) {

        Optional<Product> existingProductOpt = productRepository.findByName(productRequest.getName());

        if(existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();

            if(Boolean.TRUE.equals(existingProduct.getActive())) {
                throw new ProductNotFoundException("Product with name " + existingProduct.getName() +
                        " already exist in database");
            }

            // Reactivate and update the existing product
            updateProductFromRequest(existingProduct, productRequest);
            existingProduct.setActive(true);
            Product updatedProduct = productRepository.save(existingProduct);
            return modelMapper.map(updatedProduct,ProductResponse.class);
        }

        // No existing product — create new
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product not found with given id: " + productId));

        updateProductFromRequest(product, productRequest);

        Product updatedProduct = productRepository.save(product);

        return modelMapper.map(updatedProduct, ProductResponse.class);
    }

//    @Override
//    public Optional<ProductResponse> updateProduct(Long productId, ProductRequest productRequest) {
//
//        Product product = productRepository.findById(productId).orElseThrow(
//                () -> new RuntimeException("Product not found with the given id...")
//        );
//
//        updateProductFromRequest(product, productRequest);
//
//        Product savedProduct = productRepository.save(product);
//
//        ProductResponse productResponse = modelMapper.map(savedProduct, ProductResponse.class);
//
//        return Optional.ofNullable(productResponse);
//    }



    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public String deleteProduct(Long productId) {

        return productRepository.findById(productId)
                .map(product -> {
                        product.setActive(false);
                        productRepository.save(product);
                        return "Product deleted successfully...";
                })
                .orElseThrow(() -> new ProductNotFoundException("Product not found with given id: " + productId));
    }

    @Override
    public ApiResponse deleteProductById(Long productId) {
        productRepository.findById(productId)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return product;
                }).orElseThrow(() -> new ProductNotFoundException("Product not found with given id: " + productId));

        return new ApiResponse("Product deleted successfully..", true);
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {

        if(keyword==null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return productRepository.searchProduct(keyword.trim())
                .stream()
                .map(product -> modelMapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

//    @Override
//    public Optional<ProductResponse> getProductById(Long productId) {
//
//        Product product = productRepository.findById(productId).orElseThrow(() ->
//                new ProductNotFoundException("Product not found with given id: " + productId));
//
//        return Optional.of(modelMapper.map(product, ProductResponse.class));
//    }

    @Override
    public ProductResponse getProductById(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product not found with given id: " + productId));

        return modelMapper.map(product, ProductResponse.class);
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
    }
}
