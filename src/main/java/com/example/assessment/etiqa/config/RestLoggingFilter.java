package com.example.assessment.etiqa.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;


//filter to log the api request and response for each request
@Component
public class RestLoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(RestLoggingFilter.class);

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        logger.info("Method = {}; URI= {}", request.getMethod(), request.getRequestURI());

        filterChain.doFilter(request, responseWrapper);

        byte[] content = responseWrapper.getContentAsByteArray();
        String responseBody = new String(content, response.getCharacterEncoding());

        logger.info("Status= {}; Response = {}",response.getStatus(), responseBody);

        responseWrapper.copyBodyToResponse();
    }


}
