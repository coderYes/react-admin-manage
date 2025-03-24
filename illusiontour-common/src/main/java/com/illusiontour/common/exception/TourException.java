package com.illusiontour.common.exception;

import com.illusiontour.common.result.ResultCodeEnum;
import lombok.Data;

@Data
public class TourException extends RuntimeException {
    //异常状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     *
     * @param message
     * @param code
     */
    public TourException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public TourException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "TourException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
