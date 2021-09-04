package com.znaczek.agw.security;

import com.znaczek.agw.DebugHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

  private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

  private final DebugHelper debugHelper;

  @Override
  public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
    log.warn("Authentication failed: {}", exception.getMessage());
    URI location = UriComponentsBuilder.fromUriString("/").query("error=" + exception.getMessage()).build().toUri();

    return webFilterExchange.getExchange()
      .getSession()
      .doOnNext(s -> debugHelper.help("AUTH_FAILURE", webFilterExchange.getExchange(), s))
      .flatMap(a -> redirectStrategy.sendRedirect(webFilterExchange.getExchange(), location));
//    return redirectStrategy.sendRedirect(webFilterExchange.getExchange(), location);
  }
}
