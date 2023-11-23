package com.example.quarkus.controllers;

import com.example.quarkus.dtos.ProductRecordDTO;
import com.example.quarkus.models.ProductModel;
import com.example.quarkus.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController(value = "products/")
public class ProductController {

    public static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado.";

    final
    ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductModel> savePoduct(
            @RequestBody @Valid ProductRecordDTO productRecordDTO) throws InvocationTargetException, IllegalAccessException {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productModel, productRecordDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productRepository.save(productModel));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getProducts() {
        List<ProductModel> productModels = productRepository.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productModels);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getProduct(
            @PathVariable(value = "id") UUID id) {
        Optional<ProductModel> modelOptional = productRepository.findById(id);
        if (modelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(PRODUTO_NAO_ENCONTRADO);
        }
        ProductModel productModel = modelOptional.get();
        return ResponseEntity.status(HttpStatus.OK)
                .body(productModel);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable(value = "id") UUID id,
            @RequestBody @Valid ProductRecordDTO productRecordDTO) throws InvocationTargetException, IllegalAccessException {

        Optional<ProductModel> productModelO = productRepository.findById(id);
        if (productModelO.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(PRODUTO_NAO_ENCONTRADO);
        }

        var productModel = productModelO.get();
        BeanUtils.copyProperties(productModel, productRecordDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productRepository.save(productModel));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(
            @PathVariable(value = "id") UUID id) {

        Optional<ProductModel> productModelO = productRepository.findById(id);
        if (productModelO.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(PRODUTO_NAO_ENCONTRADO);
        }

        var productModel = productModelO.get();
        productRepository.delete(productModel);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Produto excluído");
    }
}
