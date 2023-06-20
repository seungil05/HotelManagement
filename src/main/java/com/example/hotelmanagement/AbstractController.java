package com.example.hotelmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractController {

    protected static <T> ResponseEntity<T> getRespond(T result) {
        return ResponseEntity.ok(result);
    }

    protected static <T> ResponseEntity<T> postRespond() {
        return ResponseEntity.ok().build();
    }

    protected static <T> ResponseEntity<T> putRespond() {
        return ResponseEntity.ok().build();
    }

    protected static <T> ResponseEntity<T> deleteRespond() {
        return ResponseEntity.ok().build();
    }

    protected static <T> ResponseEntity<T> badRespond() {
        return ResponseEntity.badRequest().build();
    }

    protected static <T> ResponseEntity<T> notFoundRespond() {
        return ResponseEntity.notFound().build();
    }

}
