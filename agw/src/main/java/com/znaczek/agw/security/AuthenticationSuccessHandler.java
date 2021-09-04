package com.znaczek.agw.security;

import com.znaczek.agw.DebugHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.SpanNamer;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.async.TraceRunnable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

  private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();

  private final DebugHelper debugHelper;
  private final Tracer tracer;
  private final SpanNamer spanNamer;

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    ServerWebExchange exchange = webFilterExchange.getExchange();

    return this.redirectStrategy.sendRedirect(exchange, URI.create("/?login=true"))
      .then(exchange.getSession())
      .flatMap(s -> Mono.fromRunnable(new TraceRunnable(tracer, spanNamer, () -> {
        debugHelper.help("AUTH_SUCCESS", exchange, s);
      }, "AuthenticationSuccessHandler")))
      .then();
  }
}
