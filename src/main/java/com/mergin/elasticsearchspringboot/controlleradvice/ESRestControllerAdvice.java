package com.mergin.elasticsearchspringboot.controlleradvice;

import com.mergin.elasticsearchspringboot.exception.RecordNotFoundException;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ESRestControllerAdvice {
  @ExceptionHandler(RecordNotFoundException.class)
  public ResponseEntity<Error> handleRecordNotFoundException(RecordNotFoundException exception) {
    log.info(exception.getMessage());
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<Error> handleIOException(IOException exception) {
    log.error(exception.getMessage());
    return ResponseEntity.internalServerError()
        .body(new Error(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Error> handleException(Exception exception) {
    log.error(exception.getMessage());
    return ResponseEntity.internalServerError()
        .body(new Error(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }

  @Getter
  @Setter
  @AllArgsConstructor
  class Error {
    private String message;
    private int code;
  }
}
