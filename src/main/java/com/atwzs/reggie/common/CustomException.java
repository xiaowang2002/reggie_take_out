package com.atwzs.reggie.common;

/**
 * @ClassName CustomException
 * @Description 自定义异常类
 * @Author WangZhisheng
 * @Date 15:36 2022/12/29
 * @Version 11.0.15
 */
public class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }
}
