package com.assignment.mlapi.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

/**
 * Created by shashwat on 4/25/18.
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MLApiExceptionHandler extends ResponseEntityExceptionHandler
{
    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError)
    {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(MLApiException.class)
    protected ResponseEntity<ApiError> handleEntityNotFound(
            MLApiException ex)
    {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return buildResponseEntity(apiError);
    }
}
