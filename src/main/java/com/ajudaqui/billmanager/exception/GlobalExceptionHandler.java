package com.ajudaqui.billmanager.exception;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());

  // Para tratar ausencia de parametros em objetos validado na chamada de enpoints
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseError> handleValidationExceptions(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .findFirst()
        .orElse("Erro de validação");

    return ResponseEntity.badRequest().body(new ResponseError(errorMessage));
  }

  // Para tratar ausencia de parametros na chamada de endpoints
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Object> handleMissingRequestParam(MissingServletRequestParameterException ex) {
    String name = ex.getParameterName();
    return ResponseEntity
        .badRequest()
        .body(new ResponseError("O parâmetro obrigatório '" + name + "' está ausente."));
  }

  // Para tratar ausencia de valor no path variable
  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<Map<String, String>> handleMissingPathVariable(MissingPathVariableException ex) {
    String name = ex.getVariableName();

    Map<String, String> error = new HashMap<>();
    error.put("error", "Variável de caminho ausente");
    error.put("param", name);

    return ResponseEntity.badRequest().body(error);
  }

  // Trata rota não cadastrada... lembrar de atualizar o properties
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(NoHandlerFoundException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Rota não encontrada");
    error.put("path", ex.getRequestURL());
    return ResponseEntity.status(NOT_FOUND).body(error);
  }

  // Tratar exception em geral:
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception exception) {
    HttpStatus status = determineHttpStatus(exception);
    infoTrace(exception);
    return new ResponseEntity<>(new ApiErrorResponse(exception.getMessage(), status.value()), status);
  }

  // tratar a validação do localdate
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ResponseError> handleInvalidFormat(HttpMessageNotReadableException ex) {
    Throwable cause = ex.getCause();
    if (cause instanceof InvalidFormatException) {
      return ResponseEntity
          .badRequest()
          .body(new ResponseError("Formato de data inválido. Use o padrão yyyy-MM-dd."));
    }
    return ResponseEntity
        .badRequest()
        .body(new ResponseError("Erro ao ler a requisição. Verifique os dados enviados."));
  }

  private void infoTrace(Exception exception) {
    StackTraceElement element = exception.getStackTrace()[0];
    StackTraceElement callElement = exception.getStackTrace()[1];

    logger.error("Exception occurred at: [{}] {}, line: {} with error Details: [{}] {}, line: {} | {}",
        callElement.getFileName(), callElement.getMethodName(),
        callElement.getLineNumber(), element.getFileName(), element.getMethodName(),
        element.getLineNumber(), exception.getMessage());

  }

  private HttpStatus determineHttpStatus(Exception exception) {
    return EXCEPTION_STATUS.getOrDefault(exception.getClass(), INTERNAL_SERVER_ERROR);
  }

  // Map das classes de exeção
  private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS = new HashMap<>();

  static {
    EXCEPTION_STATUS.put(FeignException.class, BAD_GATEWAY);

    EXCEPTION_STATUS.put(MsgException.class, BAD_REQUEST);
    EXCEPTION_STATUS.put(IllegalArgumentException.class, BAD_REQUEST);
    EXCEPTION_STATUS.put(NullPointerException.class, BAD_REQUEST);

    EXCEPTION_STATUS.put(IOException.class, INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS.put(IndexOutOfBoundsException.class, INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS.put(RuntimeException.class, INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS.put(Exception.class, INTERNAL_SERVER_ERROR);
    EXCEPTION_STATUS.put(NotFoundEntityException.class, NOT_FOUND);
  }
}
