package com.example.hotelmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController {

    protected static <T> ResponseEntity<T> getRespond(T result) {
        return ResponseEntity.ok(result);
    }
}
