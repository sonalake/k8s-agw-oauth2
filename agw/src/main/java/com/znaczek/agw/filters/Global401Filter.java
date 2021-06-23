package com.znaczek.agw.filters;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Global401Filter {

  // /oauth2/authorization is hardcoded in spring security reactive
  private final String uriBase = "/oauth2/authorization/iam";
  private final String [] htmlAcceptTypes = {"text/html","application/xhtml+xml"};

  @Bean
  public GlobalFilter postGlobalFilter() {
    return (exchange, chain) -> chain.filter(exchange)
      .then(Mono.fromRunnable(() -> {
          ServerHttpResponse response = exchange.getResponse();
          ServerHttpRequest request = exchange.getRequest();
          if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode()) && shouldRedirectToLoginPage(request)) {
            throw new ClientAuthorizationRequiredException("iam");
          }
        })
      );
  }

  private boolean shouldRedirectToLoginPage(ServerHttpRequest request) {
    return HttpMethod.GET.equals(request.getMethod()) &&
      !isXHR(request) &&
      isBrowserTopNavigationHTMLRequest(request);
  }

  private boolean isXHR(ServerHttpRequest request) {
    return Optional.ofNullable(request.getHeaders().get("X-Requested-With"))
      .orElseGet(Collections::emptyList)
      .contains("XMLHttpRequest");
  }

  private boolean isBrowserTopNavigationHTMLRequest(ServerHttpRequest request) {
    List<String> accept = Optional.ofNullable(request.getHeaders().get("Accept"))
      .orElseGet(Collections::emptyList);
    return accept.stream().anyMatch(a -> Arrays.stream(htmlAcceptTypes).anyMatch(a::contains));
  }
}
