package com.ajudaqui.billmanager.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @Configuration
// @WebFilter(value = "/*", asyncSupported = true) // colocar o path da requisição que recebe arquivos de ate 100 mb
public class FilterRequest implements Filter {
  // ele verifica as requisiçoẽs http antes de chegar a classe
  private static final Logger logger = LoggerFactory.getLogger(FilterRequest.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String authorization = httpRequest.getHeader("Authorization");
    logger.info("[{}] | {} | authorization: {}",
        httpRequest.getMethod(),
        httpRequest.getRequestURI(),
        authorization);

    chain.doFilter(request, response);
  }

}
