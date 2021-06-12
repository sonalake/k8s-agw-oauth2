package com.znaczek.agw.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.net.URI;

@Data
@AllArgsConstructor
public class NoAuthResponse {
  private URI location;
  private HttpStatus status;
}
