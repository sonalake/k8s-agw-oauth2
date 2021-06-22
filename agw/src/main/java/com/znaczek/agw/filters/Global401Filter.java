package com.znaczek.agw.filters;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Global401Filter {

  // /oauth2/authorization is hardcoded in spring security reactive
  private final String uriBase = "/oauth2/authorization/iam";

  @Bean
  public GlobalFilter postGlobalFilter() {
    return (exchange, chain) -> chain.filter(exchange)
      .then(Mono.fromRunnable(() -> {
          ServerHttpResponse response = exchange.getResponse();
          ServerHttpRequest request = exchange.getRequest();
          boolean isXhr = Optional.ofNullable(request.getHeaders().get("X-Requested-With"))
            .orElseGet(Collections::emptyList)
            .contains("XMLHttpRequest");

          if (response.getRawStatusCode().toString().equals("401") && !isXhr) {
            throw new ClientAuthorizationRequiredException("iam");
          }
        })
      );
  }
}
