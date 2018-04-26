package com.assignment.mlapi.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Created by shashwat on 4/25/18.
 */
public class ApiError
{
    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
//    private List<ApiSubError> subErrors;

    ApiError(HttpStatus status, String message)
    {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus()
    {
        return status;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public String getMessage()
    {
        return message;
    }
}
