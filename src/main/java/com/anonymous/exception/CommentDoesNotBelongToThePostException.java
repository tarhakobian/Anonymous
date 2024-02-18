package com.anonymous.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Comment does not belong to the Post")
public class CommentDoesNotBelongToThePostException extends RuntimeException {
}
