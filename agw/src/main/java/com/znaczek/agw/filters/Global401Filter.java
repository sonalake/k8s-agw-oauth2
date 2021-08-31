package com.znaczek.agw.filters;


import com.znaczek.agw.security.LoginLinksProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import static com.znaczek.agw.security.SecurityConfig.AUTH_ENTRYPOINT_HEADER_NAME;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Global401Filter {

  private final LoginLinksProvider loginLinksProvider;

  @Bean
  public GlobalFilter postGlobalFilter() {
    return (exchange, chain) -> chain.filter(exchange)
      .then(Mono.fromRunnable(() -> {
          ServerHttpResponse response = exchange.getResponse();
          if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
            response.getHeaders().add(AUTH_ENTRYPOINT_HEADER_NAME, loginLinksProvider.provide());
          }
        })
      );
  }

}
