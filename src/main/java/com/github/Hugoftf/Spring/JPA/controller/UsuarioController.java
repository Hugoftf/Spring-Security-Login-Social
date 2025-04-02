package com.github.Hugoftf.Spring.JPA.controller;

import com.github.Hugoftf.Spring.JPA.controller.dto.UsuarioDTO;
import com.github.Hugoftf.Spring.JPA.controller.mappers.UsuarioMapper;
import com.github.Hugoftf.Spring.JPA.model.Usuario;
import com.github.Hugoftf.Spring.JPA.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    public final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper){
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarUsuario(@RequestBody @Valid UsuarioDTO dto){
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuarioService.salvar(usuario);
    }
}
