package com.atwzs.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.atwzs.reggie.common.BaseContext;
import com.atwzs.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginCheckFilter
 * @Description 登录检查，检查用户是否完成了登录
 * @Author WangZhisheng
 * @Date 18:21 2022/12/27
 * @Version 11.0.15
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();   //路径匹配器，支持通配符

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;  //强转,因为ServletRequest没有geturi方法
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        1、获取本次请求的URI
        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}",requestURI);

        String[] urls = new String[]{
                "/employee/login", "/employee/logout", "/backend/**", "/front/**","/common/**"
                ,"/user/sendMsg","/user/login",""
        };       //定义不需要处理的请求路径
//        2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

//        3、如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request, response);
            return;
        }

////        4、判断登录状态，如果已登录，则直接放行
//        if (request.getSession().getAttribute("employee") != null) {
//            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
//            log.info("线程Id为：{}",Thread.currentThread().getId());
//            Long empId = (Long) request.getSession().getAttribute("employee");
//            BaseContext.setCurrentId(empId);
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        log.info("用户未登录");
////        5、如果未登录则返回未登录结果,通过输出流方式向客户端页面响应数据,因为通过request.js切换页面
//        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        //        4、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            log.info("线程Id为：{}",Thread.currentThread().getId());
            Long empId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录");
//        5、如果未登录则返回未登录结果,通过输出流方式向客户端页面响应数据,因为通过request.js切换页面
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * @description: 路径匹配，检查本次请求是否需要放行
     **/
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
