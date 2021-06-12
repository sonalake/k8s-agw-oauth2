package com.znaczek.agw.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final OAuthProperties oAuthProperties;

  public NoAuthResponse noAuth(String host, List<MediaType> mediaTypes) {
    return new NoAuthResponse(
      getLoginUri(host),
      mediaTypes.contains(MediaType.APPLICATION_JSON) ? HttpStatus.UNAUTHORIZED : HttpStatus.FOUND);
  }

  private URI getLoginUri(String host) {
    return UriComponentsBuilder.fromUriString(oAuthProperties.getAuthorizationUri())
      .queryParam(OAuth2ParameterNames.CLIENT_ID, oAuthProperties.getClientId())
      .queryParam(OAuth2ParameterNames.STATE, "sampleState")
      .queryParam(OAuth2ParameterNames.REDIRECT_URI, host)
      .build().toUri();
  }
}
