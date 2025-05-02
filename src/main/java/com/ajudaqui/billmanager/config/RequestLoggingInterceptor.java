package com.ajudaqui.billmanager.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    if (handler instanceof HandlerMethod) {

      String authHeader = "";

      if (request.getHeader("Authorization") != null) {
        authHeader = "| token: " + request.getHeader("Authorization");
      }

      HandlerMethod method = (HandlerMethod) handler;
      String controller = method.getBeanType().getSimpleName();
      String methodName = method.getMethod().getName();
      String path = request.getRequestURI();
      String httpMethod = request.getMethod();
      String ip = request.getRemoteAddr();
      logger.info("{} | {} | {} : [{}] {} {}", ip, controller, methodName, httpMethod, path, authHeader);
    }

    return true;
  }

}
