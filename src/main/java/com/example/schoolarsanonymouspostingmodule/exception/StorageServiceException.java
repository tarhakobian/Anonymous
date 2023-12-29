package com.example.schoolarsanonymouspostingmodule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Internal Storage Service Exception", code = HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageServiceException extends RuntimeException {
}
