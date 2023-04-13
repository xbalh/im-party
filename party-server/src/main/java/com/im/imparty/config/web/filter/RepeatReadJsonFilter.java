package com.im.imparty.config.web.filter;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Order(0)
@WebFilter(urlPatterns = "/*")
public class RepeatReadJsonFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //请求参数为JSON类型，则使用自定义包装
        if(request instanceof HttpServletRequest
                && "application/json".equals(((HttpServletRequest)request).getHeader("Content-Type"))) {
            chain.doFilter(new RepeatReadFormRequestWrapper((HttpServletRequest) request), response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
