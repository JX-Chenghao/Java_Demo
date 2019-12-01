package com.ncu.springboot.mvc.exception;

import com.ncu.springboot.pojo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.SystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebResult;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //自定义的异常
    @ExceptionHandler(OwnException.class)
    @ResponseBody
    public ResponseVo customHandler(OwnException e){
        return getResponseVoForOwnBiz(e);
    }
    //未处理的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseVo exceptionHandler(Exception e){
        return getResponseVoForOther(e);
    }

    private ResponseVo getResponseVoForOwnBiz(Exception e) {
        log.info("错误：{}", e);
        ResponseVo responseVo = new ResponseVo();
        responseVo.setErrorCode("BIZ_ERROR");
        responseVo.setMessage(e.getMessage());
        responseVo.setResult("exception");
        return responseVo;
    }


    private ResponseVo getResponseVoForOther(Exception e) {
        log.info("错误：{}", e);
        ResponseVo responseVo = new ResponseVo();
        responseVo.setErrorCode("SYS_ERROR");
        responseVo.setMessage(e.getMessage());
        responseVo.setResult("exception");
        return responseVo;
    }
}

