package com.study.fun.portal.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException,
            ServletException {
        response.setContentType("application/json");
        response.getWriter().println("{\"code\":99,\"message\":\"fail\",\"data\":\"" + accessDeniedException.getMessage() + "\"}");
        response.getWriter().flush();
    }
}
