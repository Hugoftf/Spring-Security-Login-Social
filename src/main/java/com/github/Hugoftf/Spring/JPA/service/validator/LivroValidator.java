package com.github.Hugoftf.Spring.JPA.service.validator;

import com.github.Hugoftf.Spring.JPA.exceptions.RegistroDuplicadoException;
import com.github.Hugoftf.Spring.JPA.model.Livro;
import com.github.Hugoftf.Spring.JPA.repository.LivroRepository;
import org.springframework.stereotype.Component;

@Component
public class LivroValidator {

    private LivroRepository livroRepository;

    public LivroValidator(LivroRepository livroRepository){
        this.livroRepository = livroRepository;
    }

    public void validandoLivro(Livro livro){
        if(existeLivroComISBN(livro)){
            throw new RegistroDuplicadoException("Já existe livro com esse ISBN");
        }
    }

    public boolean existeLivroComISBN(Livro livro){
       var encotnrado = livroRepository.findByIsbn(livro.getIsbn());
       if (livro.getId() == null){
           return encotnrado.isPresent();
       }

       return encotnrado.map(Livro::getId).stream().anyMatch(id -> !id.equals(livro.getId()));

    }
}
