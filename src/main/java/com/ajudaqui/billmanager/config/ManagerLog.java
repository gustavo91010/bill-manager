
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

import org.springframework.context.annotation.Configuration;

@Configuration
@WebFilter(value = "/*", asyncSupported = true) // colocar o path da requisição que recebe arquivos de ate 100 mb
public class ManagerLog implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    System.out.println("------------------------");

    System.out.println();
    System.out.println("kekeke");
    System.out.println("Path: " + httpRequest.getRequestURI());
    System.out.println("verbo: " + httpRequest.getMethod());
    // System.out.println("hum... " + httpRequest.getPathInfo());
    // System.out.println("hum... " + httpRequest.getReader());
    // System.out.println("hum... " + httpRequest.getAuthType());
    System.out.println("hum... " + httpRequest.getLocalAddr());
    System.out.println("hum... " + httpRequest.getLocalName());
    System.out.println("hum... " + httpRequest.getClass());
    System.out.println("hum... " + httpRequest.toString());


    // System.out.println("lalala "+httpRequest.getParameterMap());
    // System.out.println("lalala "+httpRequest.getParameterNames());
    // System.out.println("lalala "+httpRequest.getAttributeNames());
    System.out.println("Path completo com porta "+httpRequest.getRequestURL());
    System.out.println("Path completo sem porta "+httpRequest.getServletPath());



    String authorization = httpRequest.getHeader("Authorization");
    System.out.println("accessToken " + authorization);
    String timestamp = java.time.LocalDateTime.now()
        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    System.out.println("lalala");
    System.out.println(httpRequest);
    String contentLengthHeader = httpRequest.getHeader("Content-Length");

    System.out.printf("%s | [%s] | %s | %s%n", timestamp, httpRequest.getMethod(),
        httpRequest.getRequestURI(), authorization);

    System.out.println("contentLengthHeader " + contentLengthHeader);
    System.out.println();
    System.out.println();

    chain.doFilter(request, response);
  }

}
