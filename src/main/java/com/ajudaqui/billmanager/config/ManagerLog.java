package com.ajudaqui.billmanager.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
@WebFilter(value = "/*", asyncSupported = true) // colocar o path da requisição que recebe arquivos de ate 100 mb
public class ManagerLog implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(ManagerLog.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String authorization = httpRequest.getHeader("Authorization");
    System.out.println(httpRequest);
    // logger.info("[{}] | {} | {} | authorization: {}",
    logger.info("[{}] | {} | authorization: {}",
        httpRequest.getMethod(),
        httpRequest.getRequestURI(),
        // java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd
        // HH:mm:ss")),
        authorization);

    chain.doFilter(request, response);
  }

}
