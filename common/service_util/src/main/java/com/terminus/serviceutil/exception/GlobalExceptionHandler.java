package com.terminus.serviceutil.exception;

import com.terminus.commonutil.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 捕获自定义异常
     */
    @ExceptionHandler(AppointmentException.class)
    @ResponseBody
    public Result error(AppointmentException appointmentException) {
        return Result.build(appointmentException.getCode(), appointmentException.getMessage());
    }
}
