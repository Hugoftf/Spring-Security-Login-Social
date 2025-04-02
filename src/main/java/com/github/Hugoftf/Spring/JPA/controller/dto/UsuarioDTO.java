package com.github.Hugoftf.Spring.JPA.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UsuarioDTO(
        @NotBlank(message = "Campo obrigatório")
        String login,
        @Email(message = "Email invalido")
        @NotBlank(message = "Campo obrigatorio")
        String email,
        @NotBlank(message = "Campo obrigatorio")
        String senha,
        List<String> roles) {
}
