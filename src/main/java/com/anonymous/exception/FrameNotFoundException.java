package com.anonymous.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Couldn't find frame bby provided id")
public class FrameNotFoundException extends RuntimeException {
}
