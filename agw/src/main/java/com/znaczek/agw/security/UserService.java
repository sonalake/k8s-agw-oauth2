package com.znaczek.agw.security;

import com.nimbusds.jwt.JWTParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

  @Value("${msagw.name-claim}")
  private String nameClaim;

  @Value("${msagw.roles-claim}")
  private String rolesClaim;

  @Override
  public Mono<OAuth2User> loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    String token = oAuth2UserRequest.getAccessToken().getTokenValue();
    List<GrantedAuthority> authorities;
    Map<String, Object> attributes;
    try {
      var jwt = JWTParser.parse(oAuth2UserRequest.getAccessToken().getTokenValue());
      var claims = jwt.getJWTClaimsSet().getClaims();
      authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String)claims.get(rolesClaim));
      attributes = Stream.of(
        oAuth2UserRequest.getAdditionalParameters(),
        Map.of(
          "name", claims.get(nameClaim),
          "roles", authorities
        )
      )
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    } catch (Exception e) {
      OAuth2Error oauth2Error = new OAuth2Error("invalid_token", "Couldn't parse the token: " + token, null);
      throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
    }

    final OAuth2User user = new DefaultOAuth2User(authorities, attributes, "name");
    return Mono.just(user);
  }
}
