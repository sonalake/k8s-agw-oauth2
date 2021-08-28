package com.znaczek.agw.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class LangResolver {

  private Map<String, String> availableLangs;

  @Autowired
  private void setAvailableLocales(@Value("${msagw.available-locales}") String availableLangs) {
    this.availableLangs = Arrays.stream(availableLangs.split(","))
      .map(l -> l.split(";"))
      .collect(Collectors.toMap(l -> l[0], l -> l[1]));
  }

  private static final Pattern pathLangPattern = Pattern.compile("^/([a-z]{2})(/.*|$)");

  public String resolveISOLang(ServerHttpRequest request) {
    String pathLang = availableLangs.get(getFromPath(request));
    if (pathLang != null) {
      return pathLang;
    }

    String cookieLang = getFromAccept(getCookieAccept(request));
    if (cookieLang != null) {
      return cookieLang;
    }

    String headerLang = getFromAccept(getHeaderAccept(request));
    if (headerLang != null) {
      return headerLang;
    }

    return "en-gb";
  }

  private String getFromPath(ServerHttpRequest request) {
    String path = request.getPath().pathWithinApplication().value();
    Matcher m = pathLangPattern.matcher(path);
    return m.matches() ? m.group(1) : null;
  }

  private String getCookieAccept(ServerHttpRequest request) {
    return Optional.ofNullable(request.getCookies().getFirst(HttpHeaders.ACCEPT_LANGUAGE))
      .orElseGet(() -> new HttpCookie(HttpHeaders.ACCEPT_LANGUAGE, null)).getValue();
  }

  private String getHeaderAccept(ServerHttpRequest request) {
    return request.getHeaders().getFirst(HttpHeaders.ACCEPT_LANGUAGE);
  }

  private String getFromAccept(String accept) {
    String[] a = accept.split(",");
    if (a.length == 0) {
      return null;
    }

    a = a[0].split(";");

    if (a.length == 0) {
      return null;
    }
    return availableLangs.containsValue(a[0]) ? a[0] : null;
  }

  private String getLangKey(String value) {
    for (var entry : availableLangs.entrySet()) {
      if (entry.getValue().equals(value)) {
        return entry.getKey();
      }
    }
    return null;
  }

}
