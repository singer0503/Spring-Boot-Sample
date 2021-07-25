package com.example.demo.controller;

import com.example.demo.entity.Product;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     // @RestController 標記，代表這是一個 Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)    // 藉由參數來定義回傳的資料格式為 JSON。
public class ProductController {

    @GetMapping("/products/{id}")       // @GetMapping 標記，再傳入資源路徑，即可配置一個 GET 請求 的 API
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id){   //透過 @PathVariable 標記，獲取路徑中的 id 值。
        Product product = new Product();
        product.setId(id);
        product.setName("Max");
        product.setPrice(135);

        // Spring Boot 提供了「回應實體」類別（ResponseEntity），讓開發者能將要回應的各種資訊，以一個物件來包裝
        return ResponseEntity.ok().body(product);
    }
}
