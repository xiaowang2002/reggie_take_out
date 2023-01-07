package com.atwzs.reggie.common;

/**
 * @ClassName BaseContext
 * @Description 基于ThreadLocal封装的工具类，用于保存或获取当前登录用户的id
 * @Author WangZhisheng
 * @Date 10:29 2022/12/29
 * @Version 11.0.15
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
