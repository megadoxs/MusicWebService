package com.champlain.apigatewayservice.utils;

import com.champlain.apigatewayservice.utils.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public HttpErrorInfo handleNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicateUserPlaylist.class)
    public HttpErrorInfo handleDuplicateUserPlaylistException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicateSongTitleException.class)
    public HttpErrorInfo handleDuplicateSongTitleException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicateStageNameException.class)
    public HttpErrorInfo handleDuplicateStageNameException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicateUsernameException.class)
    public HttpErrorInfo handleDuplicateUsernameException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        return new HttpErrorInfo(httpStatus, request.getDescription(false), ex.getMessage());
    }
}
