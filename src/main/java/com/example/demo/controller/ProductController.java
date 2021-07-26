package com.example.demo.controller;

import com.example.demo.entity.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
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

    @PostMapping("/products")   // 使用 @PostMapping 標記可配置 POST 請求的 API
    public ResponseEntity<Product> createProduct(@RequestBody Product request){ //透過 @RequestBody 標記來接收前端送來的請求主體（body）。Spring Boot 會將請求主體的 JSON 字串轉換為該資料型態的物件。

        // 首先檢查該 id 是否重複，是的話就回傳狀態碼422（Unprocessable Entity），否則就存進 List。
        boolean isIdDuplicated = productDB.stream().anyMatch(p ->p.getId().equals(request.getId()));
        if(isIdDuplicated){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.add(product);

        // 一般會回應狀態碼201（Created），並附上指向這個新資源的 URI。這裡透過 ServletUriComponentsBuilder 來建立 URI。
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().path("/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(location).body(product);

    }
}
