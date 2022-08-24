package com.jiao.exception;

import com.jiao.vo.ResBean;
import com.jiao.vo.ResBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * auth:@highSky
 * create:2022/8/13
 * email:zgt9321@qq.com
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResBean ExceptionHandler(Exception e) {
        if (e instanceof GlobalException) {
            GlobalException globalException = (GlobalException) e;
            return ResBean.error(globalException.getResBeanEnum());
        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            ResBean resBean = ResBean.error(ResBeanEnum.BIND_ERROR);
            resBean.setMessage(bindException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return resBean;
        }
        return ResBean.error(ResBeanEnum.FAil);
    }

}
