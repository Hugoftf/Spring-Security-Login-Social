package com.github.Hugoftf.Spring.JPA.controller.mappers;

import com.github.Hugoftf.Spring.JPA.controller.dto.UsuarioDTO;
import com.github.Hugoftf.Spring.JPA.model.Usuario;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);
}
