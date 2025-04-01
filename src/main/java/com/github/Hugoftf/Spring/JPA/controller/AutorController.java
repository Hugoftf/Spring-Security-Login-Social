package com.github.Hugoftf.Spring.JPA.controller;


import com.github.Hugoftf.Spring.JPA.controller.dto.AutorDTO;
import com.github.Hugoftf.Spring.JPA.controller.dto.ErroResposta;
import com.github.Hugoftf.Spring.JPA.controller.mappers.AutorMapper;
import com.github.Hugoftf.Spring.JPA.exceptions.OperacaoNaoPermitida;
import com.github.Hugoftf.Spring.JPA.exceptions.RegistroDuplicadoException;
import com.github.Hugoftf.Spring.JPA.model.Autor;
import com.github.Hugoftf.Spring.JPA.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("autores")
public class AutorController {

    public AutorService autorService;
    private AutorMapper autorMapper;

    public AutorController(AutorService autorService, AutorMapper autorMapper){
        this.autorService = autorService;
        this.autorMapper = autorMapper;
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> salvar(@RequestBody @Valid AutorDTO autorDTO){

        try {
            Autor autorMapeado = autorMapper.toEntity(autorDTO);
            autorService.salvar(autorMapeado);

            //http://localhost:8080/autores/(ID AQUI)
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(autorMapeado.getId())
                    .toUri();

            return ResponseEntity.created(uri).build();
        }
        catch (RegistroDuplicadoException e){
            var erroDTO = ErroResposta.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){
        UUID idAutor = UUID.fromString(id);

        return autorService.obterDetalhes(idAutor)
                .map(autor -> {AutorDTO autorDTO = autorMapper.toDTO(autor);
                return ResponseEntity.ok(autorDTO);
                }).orElseGet(()-> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> deletarAutor(@PathVariable("id") String id){
       try {
           var idColetado = UUID.fromString(id);
           Optional<Autor> autorOptional = autorService.obterDetalhes(idColetado);

           if (autorOptional.isEmpty()) {
               return ResponseEntity.notFound().build();
           }

           autorService.deletar(autorOptional.get());
           return ResponseEntity.noContent().build();
       }catch (OperacaoNaoPermitida e){
           var erroDTO = ErroResposta.conflito(e.getMessage());
           return ResponseEntity.status(erroDTO.status()).body(erroDTO);
       }

    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<AutorDTO>> encontrandoPorNomeOuNacionalidade(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade
    ){
        List<Autor> resultadoPesquisa = autorService.pesquisaByExample(nome,
                nacionalidade);

        List<AutorDTO> autorDTOList = resultadoPesquisa
                .stream()
                .map(autor -> new AutorDTO(autor.getId(), autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(autorDTOList);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> atualizandoAutor(@PathVariable("id") String id,
                                                   @RequestBody @Valid AutorDTO autorDTO){

        try {
            var idColetado = UUID.fromString(id);
            Optional<Autor> autorOptional = autorService.obterDetalhes(idColetado);
            if (autorOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            var autor = autorOptional.get();

            autor.setNome(autorDTO.nome());
            autor.setNacionalidade(autorDTO.nacionalidade());
            autor.setDataNascimento(autorDTO.dataNascimento());

            autorService.atualizar(autor);

            return ResponseEntity.noContent().build();

        }catch (RegistroDuplicadoException e){
            var erroDTO = ErroResposta.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

    }

}
