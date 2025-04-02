package com.github.Hugoftf.Spring.JPA.security;

import com.github.Hugoftf.Spring.JPA.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthentication implements Authentication {

    private final Usuario usuario;

    public CustomAuthentication(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return usuario;
    }

    @Override
    public Object getPrincipal() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
