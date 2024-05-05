package com.mawe.webapp.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

    @Value("${app.client.url}")
    private String clientAppUrl = "";

    public SimpleCorsFilter() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response2 = (HttpServletResponse) response;
        HttpServletRequest request2 = (HttpServletRequest) request;
        Map<String, String> map = new HashMap<>();
        String originHeader = request2.getHeader("origin");
        response2.setHeader("Access-Control-Allow-Origin", originHeader);
        response2.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response2.setHeader("Access-Control-Max-Age", "3600");
        response2.setHeader("Access-Control-Allow-Headers", "*");

        if ("OPTION".equalsIgnoreCase(request2.getMethod())) {
            response2.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }

}
