package com.example.schoolarsanonymouspostingmodule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Comment and Post are not related to each other")
public class CommentNotRelatedToThePostException extends RuntimeException {
}
