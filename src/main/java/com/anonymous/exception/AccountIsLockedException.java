package com.anonymous.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Account is deactivated")
public class AccountIsLockedException extends RuntimeException {
}
