package com.kukulam.githubspring.infrastracture;

import com.kukulam.githubspring.infrastracture.github.NotFoundUserRepositoryException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class ExceptionHandling {

    @ExceptionHandler({ NotFoundUserRepositoryException.class })
    ResponseEntity<String> handleNoRepositoryForUser() {
        return ResponseEntity.notFound().build();
    }

}
