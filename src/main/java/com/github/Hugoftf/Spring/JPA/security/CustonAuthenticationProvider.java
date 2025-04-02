package com.github.Hugoftf.Spring.JPA.security;

import com.github.Hugoftf.Spring.JPA.model.Usuario;
import com.github.Hugoftf.Spring.JPA.service.UsuarioService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustonAuthenticationProvider implements AuthenticationProvider {


    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public CustonAuthenticationProvider(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString();

        Usuario usuarioEcontrado = usuarioService.obterPorLogin(login);

        if (usuarioEcontrado == null) {
            throw new UsernameNotFoundException("Usuario ou senha incorretos!");
        }
        String senhaCriptografada = usuarioEcontrado.getSenha();

        boolean senharBatem = passwordEncoder.matches(senhaDigitada, senhaCriptografada);

        if (senharBatem){
            return new CustomAuthentication(usuarioEcontrado);
        }
        throw new UsernameNotFoundException("Usuario ou senha incorretos!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
