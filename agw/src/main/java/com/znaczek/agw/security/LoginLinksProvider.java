package com.znaczek.agw.security;

import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;

@Component
public class LoginLinksProvider {

  /**
   * These links are build internally in Spring Security {@link ServerHttpSecurity.OAuth2LoginSpec#getLinks}.
   * Unfortunately they are private, so the only thing we can do is to
   */
  public String provide () {
    return "/oauth2/authorization/iam";
  }

}
