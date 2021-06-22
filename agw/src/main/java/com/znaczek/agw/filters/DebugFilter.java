package com.znaczek.agw.filters;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.SpanNamer;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.async.TraceRunnable;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DebugFilter implements GlobalFilter {

  private final Tracer tracer;
  private final SpanNamer spanNamer;

  private final ServerOAuth2AuthorizedClientRepository authorizedClientRepository;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest r = exchange.getRequest();
    log.info("Request: {} {}", r.getMethodValue(), r.getURI());
    log.info("Session id: {}", exchange.getRequest().getCookies().get("SESSION"));

    return exchange.getPrincipal()
      .doOnNext(p -> log.info("Principal: {}", p))
      .then(exchange.getPrincipal())
      .cast(Authentication.class)
      .flatMap(a -> authorizedClientRepository.loadAuthorizedClient("iam", a, exchange))
      .cast(OAuth2AuthorizedClient.class)
      .doOnNext(ac -> log.info("Authorized client: {}", mapToString(ac)))
      .then(chain.filter(exchange))
      .then(Mono.fromRunnable(new TraceRunnable(tracer, spanNamer, () -> {
          ServerHttpResponse response = exchange.getResponse();
          log.info("Response: {}", response.getRawStatusCode());
        }, "DebugFilter"))
      );
  }

  public String mapToString(OAuth2AuthorizedClient ac) {
    if (ac == null) {
      return "Authorized client not found";
    }

    return "{" +
      "clientRegistration: " + ac.getClientRegistration() + ", " +
      "principalName: " + ac.getPrincipalName() + ", " +
      "accessToken: " + ac.getAccessToken().getTokenValue() + ", " +
      "refreshToken: " + (ac.getRefreshToken() != null ? ac.getRefreshToken().getTokenValue() : "") +
      "}";
  }

}
