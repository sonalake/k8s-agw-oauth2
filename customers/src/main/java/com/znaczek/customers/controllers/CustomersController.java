package com.znaczek.customers.controllers;

import com.znaczek.customers.dtos.CustomerDTO;
import com.znaczek.customers.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/customers"})
public class CustomersController {

    private final List<CustomerDTO> customers = List.of(
            new CustomerDTO("1", "John"),
            new CustomerDTO("2", "Mike"),
            new CustomerDTO("3", "Adam")
    );

    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> customers() {
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> customer(@PathVariable() String id) {
        return new ResponseEntity<>(customers.stream()
                .filter(u -> u.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Customer not found"))
                , HttpStatus.OK);
    }
}
