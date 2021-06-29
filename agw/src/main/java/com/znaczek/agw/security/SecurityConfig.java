package com.znaczek.agw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomAuthorizationRequestResolver customAuthorizationRequestResolver;
  private final LogoutSuccessHandler logoutSuccessHandler;

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
        .authorizationRequestResolver(customAuthorizationRequestResolver)
      )
      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
      .logout(l -> l
        .logoutSuccessHandler(logoutSuccessHandler)
      )
      .csrf()
      .disable()
      .build();
  }

}
