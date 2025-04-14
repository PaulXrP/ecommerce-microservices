package com.pranay.ecommerce.product_service.services.interfaces;

import com.pranay.ecommerce.product_service.Dtos.ApiResponse;
import com.pranay.ecommerce.product_service.Dtos.ProductRequest;
import com.pranay.ecommerce.product_service.Dtos.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(Long productId, ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    String deleteProduct(Long productId);

    ApiResponse deleteProductById(Long productId);

    List<ProductResponse> searchProducts(String keyword);

    ProductResponse getProductById(Long productId);
}
