package com.github.Hugoftf.Spring.JPA.controller.dto;

import com.github.Hugoftf.Spring.JPA.model.GeneroLivro;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(
        @ISBN
        @NotBlank(message = "Campo obrigatorio")
        String isbn,
        @NotBlank(message = "Campo obrigatorio")
        String titulo,
        @Past
        @NotNull(message = "Campo obrigatorio")
        LocalDate dataPublicacao,


        GeneroLivro genero,

        BigDecimal preco,

        @NotNull(message = "Campo obrigatorio")
        UUID idAutor
        ) {
}
