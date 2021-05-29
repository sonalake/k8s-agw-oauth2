package com.znaczek.users.controllers;

import com.znaczek.users.dtos.UserDTO;
import com.znaczek.users.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/users"})
public class UsersController {

    private final List<UserDTO> users = List.of(
            new UserDTO("1", "John"),
            new UserDTO("2", "Mike"),
            new UserDTO("3", "Adam")
    );

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserDTO>> users() {
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> user(@PathVariable() String id) {
        return new ResponseEntity<>(users.stream()
                .filter(u -> u.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new NotFoundException("User not found"))
                , HttpStatus.OK);
    }
}
