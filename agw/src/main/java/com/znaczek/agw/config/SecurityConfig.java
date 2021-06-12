package com.znaczek.agw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.server.WebSessionOAuth2ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * - logout z keykloack
 * - logout from UI when session ends
 * - ogarnąć login?error
 * - oauth2 repositories distributed (securityContextRepository, authorizedClientRepository, authorizationRequestRepository)
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private final ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;

  public SecurityConfig(ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
    this.reactiveClientRegistrationRepository = reactiveClientRegistrationRepository;
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

//    ReactiveOAuth2AuthorizedClientService authorizedClientService =
//      new InMemoryReactiveOAuth2AuthorizedClientService(reactiveClientRegistrationRepository);
//    ServerOAuth2AuthorizedClientRepository serverOAuth2AuthorizedClientRepository =
//      new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);

    ServerAuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = new WebSessionOAuth2ServerAuthorizationRequestRepository();

    return http
      .authorizeExchange(ae -> ae
        .anyExchange()
        .permitAll()
      )
      .oauth2Login()
      .and()
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
