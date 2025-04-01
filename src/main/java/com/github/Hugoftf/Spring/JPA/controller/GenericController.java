package com.github.Hugoftf.Spring.JPA.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GenericController {

    default URI gerarHearderLocation(UUID id){
        return ServletUriComponentsBuilder.
                fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
    }

}
