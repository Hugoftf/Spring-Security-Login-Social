package com.github.Hugoftf.Spring.JPA.repository;

import com.github.Hugoftf.Spring.JPA.model.Autor;
import com.github.Hugoftf.Spring.JPA.model.GeneroLivro;
import com.github.Hugoftf.Spring.JPA.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor<Livro> {


    // Query method

    // Encontrar livros por autor
    List<Livro> findByIdAutor(Autor autor);

    // Encontrar livros por titulo
    List<Livro> findByTitulo(String titulo);

    // Encontrar livros por titulo
    Optional<Livro> findByIsbn(String isbn);

    // Encontrar livros por titulo ou ISBN
    List<Livro> findByTituloOrIsbn(String titulo, String isbn);

    // Encontrar livros entre datas de publicações
    List<Livro> findByDataPublicacaoBetween(LocalDate inicio, LocalDate fim);

    //JPQL

    @Query(" select l from Livro as l order by l.titulo, l.preco" )
    List<Livro> listarTodosOrdenadosPorTituloEPreco();


    @Query("select l from Livro as l where l.genero = :nomeDoParametro order by :nomeDaOrdenacao")
    List<Livro> findByGenero(@Param("nomeDoParametro") GeneroLivro generoLivro
            ,@Param("nomeDaOrdenacao") String nome);


    @Query("select l from Livro as l where l.genero = ?1 order by ?2")
    List<Livro> findByGeneroPositional(GeneroLivro generoLivro, String nomePropriedade);

    boolean existsByIdAutor(Autor autor);

}
