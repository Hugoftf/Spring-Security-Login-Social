package com.github.Hugoftf.Spring.JPA.service;

import com.github.Hugoftf.Spring.JPA.controller.dto.AutorDTO;
import com.github.Hugoftf.Spring.JPA.exceptions.OperacaoNaoPermitida;
import com.github.Hugoftf.Spring.JPA.model.Autor;
import com.github.Hugoftf.Spring.JPA.model.Usuario;
import com.github.Hugoftf.Spring.JPA.repository.AutorRepository;
import com.github.Hugoftf.Spring.JPA.repository.LivroRepository;
import com.github.Hugoftf.Spring.JPA.security.SecurityService;
import com.github.Hugoftf.Spring.JPA.service.validator.AutorValidator;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private AutorRepository autorRepository;
    private AutorValidator autorValidator;
    private LivroRepository livroRepository;
    private SecurityService securityService;

    public AutorService(AutorRepository autorRepository, AutorValidator autorValidator,
                        LivroRepository livroRepository, SecurityService securityService){
        this.autorRepository = autorRepository;
        this.autorValidator = autorValidator;
        this.livroRepository =  livroRepository;
        this.securityService = securityService;
    }

    public Autor salvar(Autor autor){
        autorValidator.validar(autor);
        Usuario usuario = securityService.obterUsuario();
        autor.setIdUsuario(usuario);
        return autorRepository.save(autor);
    }

    public void atualizar(Autor autor){
        if (autor.getId() == null){
            throw new IllegalArgumentException("Autor não encontrado no DB");
        }
        autorValidator.validar(autor);
        autorRepository.save(autor);
    }

    public Optional<Autor> obterDetalhes(UUID id){
        return autorRepository.findById(id);
    }

    public void deletar(Autor autor) {
        if (possuiLivro(autor)){
            throw new OperacaoNaoPermitida("Não é permitido excluir um autor com livros!");
        }

        autorRepository.delete(autor);
    }



    public List<Autor> encontrarPorNomeOuNacionalidade(String nome, String nacionalidade){
        if (nome != null && nacionalidade != null){
            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade);
        }

        if (nome != null){
            return autorRepository.findByNome(nome);
        }

        if (nacionalidade != null){
            return autorRepository.findByNacionalidade(nacionalidade);
        }
        return autorRepository.findAll();
    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade){
        var autor =  new Autor();
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Autor> autorExample = Example.of(autor, matcher);
        return autorRepository.findAll(autorExample);
    }

    private boolean possuiLivro(Autor autor) {
        return livroRepository.existsByIdAutor(autor);
    }
}
