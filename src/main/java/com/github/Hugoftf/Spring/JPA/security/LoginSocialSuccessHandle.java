package com.github.Hugoftf.Spring.JPA.security;

import com.github.Hugoftf.Spring.JPA.model.Usuario;
import com.github.Hugoftf.Spring.JPA.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class LoginSocialSuccessHandle extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String SENHAPADRAO = "321";

    private final UsuarioService usuarioService;

    public LoginSocialSuccessHandle(UsuarioService usuarioService) {
        super();
        this.usuarioService = usuarioService;

    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        String email  = oAuth2User.getAttribute("email");
        Usuario usuario = usuarioService.findByEmail(email);

        if(usuario == null){
            usuario = cadastrarUsuario(email);
        }

        authentication = new CustomAuthentication(usuario);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    public Usuario cadastrarUsuario(String email){

        Usuario usuario = new Usuario();
        usuario.setLogin(encurtarLogin(email));
        usuario.setEmail(email);
        usuario.setSenha(SENHAPADRAO);
        usuario.setRoles(List.of("OPERARIO"));

        usuarioService.salvar(usuario);

        return usuario;
    }

    public String encurtarLogin(String email){
        return email.substring(0, email.indexOf("@"));
    }
}
