package com.anonymous.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "A user with the given email is already registered")
public class DuplicateUserException extends RuntimeException {
}
