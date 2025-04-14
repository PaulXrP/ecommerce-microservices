package com.pranay.ecommerce.product_service.controller;

import com.pranay.ecommerce.product_service.Dtos.ApiResponse;
import com.pranay.ecommerce.product_service.Dtos.ProductRequest;
import com.pranay.ecommerce.product_service.Dtos.ProductResponse;
import com.pranay.ecommerce.product_service.repositories.ProductRepository;
import com.pranay.ecommerce.product_service.services.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse product = productService.createProduct(productRequest);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> allProducts = productService.getAllProducts();
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse productById = productService.getProductById(id);
        return new ResponseEntity<>(productById, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@Valid @RequestBody ProductRequest productRequest,
                                                         @PathVariable Long id) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        ApiResponse response = productService.deleteProductById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProductByKeywords(@RequestParam String keyword) {
        List<ProductResponse> productResponses = productService.searchProducts(keyword);
        return new ResponseEntity<>(productResponses, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable Long id) {
        String deleteProduct = productService.deleteProduct(id);
        return new ResponseEntity<>(deleteProduct, HttpStatus.OK);
    }
}
