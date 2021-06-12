package com.znaczek.agw.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "msagw.auth")
public class OAuthProperties {
  private String clientId;
  private String clientSecret;
  private String tokenUri;
  private String authorizationUri;
}
