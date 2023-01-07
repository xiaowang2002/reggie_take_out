package com.atwzs.reggie.config;

import com.atwzs.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @ClassName WebMvcConfig
 * @Description
 * @Author WangZhisheng
 * @Date 12:28 2022/12/27
 * @Version 11.0.15
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    /**
    * @description: 设置静态资源映射，由原本的/static,/public,/meta-inf/resources,/resources
    **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
    * @description: 扩展mvc框架的消息转换器
    **/
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("开始进行静态资源映射...");

        //创建消息转换器，作用是将控制器返回的R类型数据转换为json数据，源于org.springframework.http.converter.json
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java转换为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中，0表示优先使用我们自己定义的转换器
        converters.add(0,messageConverter);
    }
}
