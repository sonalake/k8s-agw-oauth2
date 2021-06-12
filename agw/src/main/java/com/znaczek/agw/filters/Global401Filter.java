package com.znaczek.agw.filters;


import com.znaczek.agw.auth.AuthService;
import com.znaczek.agw.auth.NoAuthResponse;
import com.znaczek.agw.config.CustomAuthorizationRequestResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import org.springframework.util.MimeType;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Global401Filter {

  // /oauth2/authorization is hardcoded in spring security reactive
  private final String uriBase = "/oauth2/authorization/iam";

  private final AuthService authService;

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
//            exchange.getResponse().getHeaders().setLocation(
//              UriComponentsBuilder.fromUriString(uriBase)
//              .queryParam(CustomAuthorizationRequestResolver.ORIGIN_URL_PARAM, request.getURI())
//              .build()
//              .toUri()
//            );
//            HttpStatus status;
//            if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_JSON)) {
//              status = HttpStatus.UNAUTHORIZED;
//            } else {
//              status = HttpStatus.FOUND;
//            }
//            exchange.getResponse().setStatusCode(status);
          }
        })
      );
  }
}
