package com.atwzs.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理
 * @Author WangZhisheng
 * @Date 10:30 2022/12/28
 * @Version 11.0.15
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
//ControllerAdvice本质上是一个Component，是@Controller注解的一个增强，这个注解是Spring里面的东西，可以处理全局异常。
//annotation的意思是去拦截类上加了restcontroller或controller的类，所以只要加上了restcontroller或controller的类都会被这个处理器进行处理
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @description: 异常处理方法
     **/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exception(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());  //Duplicate entry 'yangmi2002' for key 'employee.idx_username'

        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> exception(CustomException ex) {
        log.error(ex.getMessage());  //Duplicate entry 'yangmi2002' for key 'employee.idx_username'
        return R.error(ex.getMessage());
    }
}
