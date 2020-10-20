package entity;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 描述
 *
 * @author www.itheima.com
 * @version 1.0
 * @package com.changgou.order.config *
 * @since 1.0
 */
public class FeignInterceptor implements RequestInterceptor {

    /**
     * 通过拦截器，将请求头给下一个微服务
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 开启熔断，feign会开启新的线程 requestAttributes会为空
        // 开启了熔断策略，默认是线程池隔离，会开启新的线程，需要让熔断策略改为信号凉隔离，不会开启新线程
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            //1.获取请求对象
            HttpServletRequest request = requestAttributes.getRequest();
            // 请求头等key
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                //2.获取请求对象中的所有的头信息(网关传递过来的)
                while (headerNames.hasMoreElements()) {
                    String headerKey = headerNames.nextElement();//头的名称
                    String value = request.getHeader(headerKey);//头名称对应的值
                    System.out.println("keyname:" + headerKey + "::::::::value:" + value);
                    //3.将头信息传递给fegin (restTemplate)
                    requestTemplate.header(headerKey,value);
                }
            }
        }


    }
}
