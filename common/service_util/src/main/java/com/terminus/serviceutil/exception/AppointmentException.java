package com.terminus.serviceutil.exception;

import com.terminus.commonutil.result.ResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义全局异常类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "自定义全局异常类")
public class AppointmentException extends RuntimeException {

    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     */
    public AppointmentException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     */
    public AppointmentException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "AppointmentException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
