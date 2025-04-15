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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    @Test
    void shouldReturnListOfActiveProducts_whenGetAllProductsCalled() {

        Product activeProduct = new Product();
        activeProduct.setId(1L);
        activeProduct.setName("phone");
        activeProduct.setActive(true);

        Product inactiveProduct = new Product();
        inactiveProduct.setId(2L);
        inactiveProduct.setName("tablet");
        inactiveProduct.setActive(false);

        when(productRepository.findByActiveTrue()).thenReturn(List.of(activeProduct));
        when(modelMapper.map(any(Product.class), eq(ProductResponse.class)))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0);
                    ProductResponse pr = new ProductResponse();
                    pr.setId(p.getId());
                    pr.setName(p.getName());
                    return pr;
                });

        List<ProductResponse> productResponses = productService.getAllProducts();

        assertEquals(1, productResponses.size());
        assertEquals("phone", productResponses.get(0).getName());
    }

    @Test
    void shouldReturnProduct_whenProductExistsById() {
        Product product = new Product();
        product.setId(1L);
        product.setName("MacBook");
        product.setCategory("Laptop");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(modelMapper.map(any(Product.class), eq(ProductResponse.class)))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0);
                    ProductResponse pr = new ProductResponse();
                    pr.setId(p.getId());
                    pr.setName(p.getName());
                    pr.setDescription(p.getDescription());
                    pr.setPrice(p.getPrice());
                    pr.setStockQuantity(p.getStockQuantity());
                    pr.setCategory(p.getCategory());
                    pr.setImageUrl(p.getImageUrl());
                    pr.setActive(p.getActive());
                    return pr;
                });

        ProductResponse result = productService.getProductById(1L);

        assertEquals(1L, result.getId());
        assertEquals("MacBook", result.getName());
        assertEquals("Laptop", result.getCategory());
    }

    @Test
    void shouldReturnProduct_whenProductExistsById2() {
        Product product = new Product();
        product.setId(1L);
        product.setName("iPad Pro 11th gen");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(modelMapper.map(product, ProductResponse.class))
                .thenReturn(new ProductResponse(1L, "iPad Pro 11th gen"));

        ProductResponse result = productService.getProductById(1L);

        assertEquals(1L, result.getId());
        assertEquals("iPad Pro 11th gen", result.getName());
    }

    @Test
    void shouldThrowException_whenProductNotFoundById() {
        when(productRepository.findById(34L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.getProductById(34L));
    }

    @Test
    void shouldMarkProductAsInactive_whenDeleteProductCalled() {
        Product product = new Product();
        product.setId(1L);
        product.setName("iPhone");
        product.setActive(true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        assertFalse(product.getActive());
        verify(productRepository).save(product);
    }

    @Test
    void shouldThrowException_whenDeletingNonExistentProduct() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(2L));
    }
}
