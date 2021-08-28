package com.znaczek.agw.security;

import com.znaczek.agw.i18n.LangResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {

  @Value("${spring.security.oauth2.client.provider.iam.logout-uri}")
  private String logoutUrl;

  private final LangResolver langResolver;

  @Override
  public Mono<Void> onLogoutSuccess(WebFilterExchange webExchange, Authentication authentication) {
    ServerHttpResponse response = webExchange.getExchange().getResponse();
    response.setStatusCode(HttpStatus.FOUND);
    response.getHeaders().setLocation(URI.create(logoutUrl));
    return Mono.empty();
  }

}
