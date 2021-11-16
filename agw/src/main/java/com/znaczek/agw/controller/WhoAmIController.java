package com.znaczek.agw.controller;

import com.znaczek.agw.security.EmptyAuthentication;
import com.znaczek.agw.security.LoginLinksProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.znaczek.agw.security.SecurityConfig.AUTH_ENTRYPOINT_HEADER_NAME;

@RestController
@RequestMapping("/whoami")
@RequiredArgsConstructor
public class WhoAmIController {

  private final LoginLinksProvider loginLinksProvider;

  @GetMapping()
  private Mono<ResponseEntity<Map<String, Object>>> whoami(ServerWebExchange exchange) {

    return exchange
      .getPrincipal()
      .cast(Authentication.class)
      .defaultIfEmpty(new EmptyAuthentication())
      .map(p -> {
        if (p.isAuthenticated()) {
          return new ResponseEntity<>(((OAuth2AuthenticationToken)p).getPrincipal().getAttributes(), HttpStatus.OK);
        } else {
          exchange.getResponse().getHeaders().add(AUTH_ENTRYPOINT_HEADER_NAME, loginLinksProvider.provide());
          return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
      });
  }

}
