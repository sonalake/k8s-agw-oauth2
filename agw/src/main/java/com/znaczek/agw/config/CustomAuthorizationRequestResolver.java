package com.znaczek.agw.config;

import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public class CustomAuthorizationRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

  public static String ORIGIN_URL_PARAM = "origin_url";

  private DefaultServerOAuth2AuthorizationRequestResolver defaultResolver;

  public CustomAuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
    defaultResolver = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
  }

  @Override
  public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
    return defaultResolver.resolve(exchange)
      .doOnNext(r -> {
        String origin_uri = exchange.getRequest().getQueryParams().getFirst(ORIGIN_URL_PARAM);
        int a = 5;
        OAuth2AuthorizationRequest rr = OAuth2AuthorizationRequest.from(r).additionalParameters(Map.of(ORIGIN_URL_PARAM, "ORIGIN_URL_PARAM"))
          .build();
      });
  }

  @Override
  public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
    return defaultResolver.resolve(exchange, clientRegistrationId);
  }
}
