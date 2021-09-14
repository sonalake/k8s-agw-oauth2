package com.znaczek.agw.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * After successful authentication we redirect user to "/" with `login=true` query parameter.
 * Client app can consume this parameter and perform some action after the user has logged in.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

  private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    ServerWebExchange exchange = webFilterExchange.getExchange();

    return this.redirectStrategy.sendRedirect(exchange, URI.create("/?login=true"));
  }
}
