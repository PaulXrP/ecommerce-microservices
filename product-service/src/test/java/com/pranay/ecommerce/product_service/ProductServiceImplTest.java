package com.pranay.ecommerce.product_service;

import com.pranay.ecommerce.product_service.Dtos.ProductRequest;
import com.pranay.ecommerce.product_service.Dtos.ProductResponse;
import com.pranay.ecommerce.product_service.exceptions.ProductNotFoundException;
import com.pranay.ecommerce.product_service.models.Product;
import com.pranay.ecommerce.product_service.repositories.ProductRepository;
import com.pranay.ecommerce.product_service.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldThrowException_whenProductAlreadyExistsAndIsActive() {

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("iPhone");

        Product existingProduct = new Product();
        existingProduct.setName("iPhone");
        existingProduct.setActive(true);

        when(productRepository.findByName("iPhone"))
                .thenReturn(Optional.of(existingProduct));

        assertThrows(ProductNotFoundException.class, () -> productService.createProduct(productRequest));
    }

    @Test
    void shouldUpdateAndReactivate_whenProductExistsButInactive() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("iPhone");

        Product existingProduct = new Product();
        existingProduct.setName("iPhone");
        existingProduct.setActive(false);

        Product updatedProduct = new Product();
        updatedProduct.setName("iPhone");
        updatedProduct.setActive(true);

        ProductResponse response = new ProductResponse();
        response.setName("iPhone");

        when(productRepository.findByName("iPhone")).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(modelMapper.map(updatedProduct,ProductResponse.class)).thenReturn(response);

        ProductResponse result = productService.createProduct(productRequest);

        assertEquals("iPhone", result.getName());
        verify(productRepository).save(existingProduct);

    }

    @Test
    void shouldCreateNewProduct_whenProductDoesNotExist() {
        ProductRequest request = new ProductRequest();
        request.setName("MacBook");

        Product savedProduct = new Product();
        savedProduct.setName("MacBook");

        ProductResponse response = new ProductResponse();
        response.setName("MacBook");

        when(productRepository.findByName("MacBook")).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(modelMapper.map(savedProduct, ProductResponse.class)).thenReturn(response);

        ProductResponse result = productService.createProduct(request);

        assertEquals("MacBook", result.getName());
        verify(productRepository).save(any(Product.class));
    }
}
