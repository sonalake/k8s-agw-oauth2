package com.znaczek.agw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * TODO
 * - logout z keykloack
 * - session timeout
 * - logout from UI when session ends
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public ServerOAuth2AuthorizedClientRepository authorizedClientRepository() {
    return new WebSessionServerOAuth2AuthorizedClientRepository();
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

    return http
      .authorizeExchange(ae -> ae
        .anyExchange()
        .permitAll()
      )
      .oauth2Login(l -> l
        .authorizedClientRepository(authorizedClientRepository())
      )
      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
      .logout(l -> l
        .logoutSuccessHandler((webExchange, authentication) -> {
          ServerHttpResponse response = webExchange.getExchange().getResponse();
          response.setStatusCode(HttpStatus.FOUND);
          response.getHeaders().setLocation(URI.create("/"));
          return Mono.empty();
        })
      )
      .csrf()
      .disable()
      .build();
  }

}
