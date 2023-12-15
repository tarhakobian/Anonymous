package com.example.schoolarsanonymouspostingmodule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Couldn't match post by provided ID")
public class PostNotFoundException extends RuntimeException {
}
