package com.andsemedodev.externalducmicroservice.exceptions;

import com.andsemedodev.externalducmicroservice.utilities.APIResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> mapErros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            mapErros.put(fieldName, errorMessage);
        });
        logger.error(ex.getLocalizedMessage());
        return new APIResponse.buildAPIResponse<Map<String, String>>()
                .setStatus(false)
                .setStatusText(HttpStatus.BAD_REQUEST.name())
                .setDetails(mapErros).builder();
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse<String> handleRecordNotFoundException(RecordNotFoundException ex) {
        logger.error(ex.getLocalizedMessage());
        return new APIResponse.buildAPIResponse<String>()
                .setStatus(false)
                .setStatusText(HttpStatus.NOT_FOUND.name())
                .setDetails(ex.getMessage()).builder();
    }

    @ExceptionHandler(CustomInternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<String> handleCustomInternalServerErrorException(CustomInternalServerErrorException ex) {
        logger.error(ex.getLocalizedMessage());
        return new APIResponse.buildAPIResponse<String>()
                .setStatus(false)
                .setStatusText(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .setDetails(ex.getMessage()).builder();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public APIResponse<String> handleNoResourceFound(NoResourceFoundException ex) {
        logger.error(ex.getLocalizedMessage());
        return new APIResponse.buildAPIResponse<String>()
                .setStatus(false)
                .setStatusText(HttpStatus.NOT_FOUND.name())
                .setDetails(ex.getMessage()).builder();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<String> handleUnhandledExceptions(Exception ex) {
        logger.error(ex.getLocalizedMessage());
        logger.error(ex);
        return new APIResponse.buildAPIResponse<String>()
                .setStatus(false)
                .setStatusText(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .setDetails(ex.getMessage()).builder();
    }

    @ExceptionHandler(EmptyRubricasException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<String> handleEmptyRubricas(EmptyRubricasException ex) {
        logger.error(ex.getLocalizedMessage());
        return new APIResponse.buildAPIResponse<String>()
                .setStatus(false)
                .setStatusText(HttpStatus.BAD_REQUEST.name())
                .setDetails(ex.getMessage()).builder();
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final APIResponse<String> unauthorizedException(Exception ex, WebRequest request) {

        logger.error(ex.getLocalizedMessage());
        return new APIResponse.buildAPIResponse<String>()
                .setStatus(false)
                .setStatusText(HttpStatus.UNAUTHORIZED.name())
                .setDetails(ex.getMessage()).builder();
    }
}
