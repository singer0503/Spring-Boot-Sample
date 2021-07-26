package com.example.demo.controller;

import com.example.demo.entity.Product;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController     // @RestController 標記，代表這是一個 Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)    // 藉由參數來定義回傳的資料格式為 JSON。
public class ProductController {

    // 以 Java 的 List 當作資料庫。
    private final List<Product> productDB = new ArrayList<>();

    @PostConstruct // javax 函式庫的 @PostConstruct 標記。這樣可以讓 Controller 被建立後，自動執行該方法
    private void initDB(){
        productDB.add(new Product("B0001", "Android Development (Java)", 380));
        productDB.add(new Product("B0002", "Android Development (Kotlin)", 420));
        productDB.add(new Product("B0003", "Data Structure (Java)", 250));
        productDB.add(new Product("B0004", "Finance Management", 450));
        productDB.add(new Product("B0005", "Human Resource Management", 330));
    }

    @GetMapping("/products/{id}")       // @GetMapping 標記，再傳入資源路徑，即可配置一個 GET 請求 的 API
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id){   //透過 @PathVariable 標記，獲取路徑中的 id 值。

        // 類似 LinQ 的方法取資料  根據 id 在 productDB 這個 List 尋找產品
        Optional<Product> productOp = productDB.stream().filter(p -> p.getId().equals(id)).findFirst();

        // 有找到就回傳資料（狀態碼200），否則回傳狀態碼404。
        if(productOp.isPresent()){
            Product product = productOp.get();
            return ResponseEntity.ok().body(product);
        }else{
            return ResponseEntity.notFound().build();
        }

    }
}
