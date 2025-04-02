package com.github.Hugoftf.Spring.JPA.security;

import com.github.Hugoftf.Spring.JPA.model.Usuario;
import com.github.Hugoftf.Spring.JPA.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private final UsuarioService usuarioService;

    public SecurityService(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    public Usuario obterUsuario(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String login = userDetails.getUsername();
//        return usuarioService.obterPorLogin(login);

        if (authentication instanceof CustomAuthentication customAuthentication){
            return customAuthentication.getUsuario();
        }

        return null;

    }
}
