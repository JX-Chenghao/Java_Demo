package com.ncu.springboot.mvc.exception;

import com.ncu.springboot.pojo.ResponseVo;
import org.omg.CORBA.SystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebResult;

@ControllerAdvice
public class GlobalExceptionHandler {
    //自定义的异常
    @ExceptionHandler(OwnException.class)
    @ResponseBody
    public ResponseVo customHandler(OwnException e){
        e.printStackTrace();
        return getResponseVo(e);
    }
    //未处理的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseVo exceptionHandler(Exception e){
        e.printStackTrace();
        return getResponseVo(e);
    }

    private ResponseVo getResponseVo(Exception e) {
        ResponseVo responseVo = new ResponseVo();
        responseVo.setErrorCode(e.getClass().getName());
        responseVo.setMessage(e.getMessage());
        responseVo.setResult("exception");
        return responseVo;
    }
}

