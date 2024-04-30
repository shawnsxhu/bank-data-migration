package com.pilot.srcserver.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse extends Response{
    HttpStatus httpStatus;
    String message;
}
