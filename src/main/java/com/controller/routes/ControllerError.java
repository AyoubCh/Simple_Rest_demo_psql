package com.controller.routes;

//import org.springframework.boot.autoconfigure.web.ErrorController;
//import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ACH1 on 20/05/2016.
 */




@ControllerAdvice
@ResponseBody
class myControllerAdvice {


    @ExceptionHandler(resourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    errorResponse userNotFoundExceptionHandler(resourceNotFoundException ex) {
        return new errorResponse(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    errorResponse otherExceptionHandler(Exception ex) {
        return new errorResponse(ex.getMessage());
    }

    class errorResponse {
        private String error;
        public errorResponse(String s) {
            this.error = s;
        }

        public String getError() {
            return error;
        }
    }
}


class resourceNotFoundException extends RuntimeException {

    public resourceNotFoundException(String msg) {
        super(msg);
    }
}




