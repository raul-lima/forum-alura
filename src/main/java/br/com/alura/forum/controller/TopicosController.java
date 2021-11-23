package br.com.alura.forum.controller;

import br.com.alura.forum.dto.AtualizacaoTopicoFormDto;
import br.com.alura.forum.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.dto.TopicoDto;
import br.com.alura.forum.dto.TopicoFormDto;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> lista(String nomeCurso){

        if (nomeCurso == null) {
            List<Topico> topicos = topicoRepository.findAll();

            return TopicoDto.converter(topicos);
        } else {
            List<Topico> topicos = topicoRepository.findByCurso_Nome(nomeCurso);

            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoFormDto topicoFormDto, UriComponentsBuilder uriComponentsBuilder){

        Topico topico = topicoFormDto.converter(cursoRepository);

        topicoRepository.save(topico);

        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        // Devolve um cabeçalho e uma representação do recurso que acabou de ser criado. Código 201
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    public DetalhesDoTopicoDto detalhar(@PathVariable("id") Long id){

        Topico topico = topicoRepository.getById(id);
        return new DetalhesDoTopicoDto(topico);


    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable("id") Long id, @RequestBody @Valid AtualizacaoTopicoFormDto atualizacaoTopicoFormDto){
        Topico topico = atualizacaoTopicoFormDto.atualizar(id, topicoRepository);

        return ResponseEntity.ok(new TopicoDto(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remover(@PathVariable("id") Long id){
        topicoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
