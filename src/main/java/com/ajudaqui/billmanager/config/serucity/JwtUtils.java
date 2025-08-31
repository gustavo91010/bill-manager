package com.ajudaqui.billmanager.config.serucity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${api.secert}")
  private String jwtSecret;

  public String getAccessTokenFromJwt(String token) {
    if (token.length() < 50)
      return token;

    token = token.replace("Bearer ", "");
    if (!validateJwtToken(token, jwtSecret))
      throw new RuntimeException("Token inválido");
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("access_token", String.class);
  }

  public boolean validateJwtToken(String authToken, String jwtSecret) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
