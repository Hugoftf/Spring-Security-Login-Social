package com.github.Hugoftf.Spring.JPA.repository;

import com.github.Hugoftf.Spring.JPA.model.Autor;
import com.github.Hugoftf.Spring.JPA.model.GeneroLivro;
import com.github.Hugoftf.Spring.JPA.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvar(){
        Livro livro =  new Livro();

        livro.setIsbn("999922999");
        livro.setTitulo("O cachorro");
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setDataPublicacao(LocalDate.of(2014, Month.JULY, 22));
        livro.setPreco(BigDecimal.valueOf(300));

        var autorEncontrado = autorRepository.findById(UUID.fromString(
                "3cee93c0-8cf7-43ac-9956-8a03750e854a")).orElse(null);

        livro.setIdAutor(autorEncontrado);

        livroRepository.save(livro);
    }

    @Test
    void autualizarAutorDeLivro(){
        var idLivro = UUID.fromString("9f291b77-adbd-4a31-b321-19abf590f498");
        Livro livro = livroRepository.findById(idLivro).orElse(null);

        var idAutor = UUID.fromString("3cee93c0-8cf7-43ac-9956-8a03750e854a");
        Autor autor = autorRepository.findById(idAutor).orElse(null);

        livro.setIdAutor(autor);

        livroRepository.save(livro);


    }

    @Test
    @Transactional
    void buscarLivroTest(){
        UUID idLivro  = UUID.fromString("e00ab61c-0fec-4a83-8161-dd0ca66fe59b");
        Livro livro = livroRepository.findById(idLivro).orElse(null);

        System.out.println("Livro:");
        System.out.println(livro.getTitulo());

        System.out.println("Autor:");
        System.out.println(livro.getIdAutor().getNome());


    }

    @Test
    void buscarPorTituloEPreco(){
        var livrosEncontrados =
                livroRepository.listarTodosOrdenadosPorTituloEPreco();

        livrosEncontrados.forEach(System.out::println);
    }

    @Test
    void listarPorGeneroQueryParamTeste(){
        var livrosEncontrados =
                livroRepository.findByGenero(GeneroLivro.ROMANCE, "dataPublicacao");

        livrosEncontrados.forEach(System.out::println);
    }

}