package com.ua.polishchuk.controller;

import com.ua.polishchuk.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ExceptionDto> handleEntityNotFound(RuntimeException e) {
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), HttpStatus.NOT_FOUND);

        return ResponseEntity
                .status(exceptionDto.getHttpStatus())
                .body(exceptionDto);
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<ExceptionDto> handleEntityExists(RuntimeException e) {
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), HttpStatus.NOT_FOUND);

        return ResponseEntity
                .status(exceptionDto.getHttpStatus())
                .body(exceptionDto);
    }

    @ExceptionHandler({NonUniqueResultException.class})
    public ResponseEntity<ExceptionDto> handleNonUniqueResult(RuntimeException e) {
        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage(), HttpStatus.NOT_FOUND);

        return ResponseEntity
                .status(exceptionDto.getHttpStatus())
                .body(exceptionDto);
    }
}
