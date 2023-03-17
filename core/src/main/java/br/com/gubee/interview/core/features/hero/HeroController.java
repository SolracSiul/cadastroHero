package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.HeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroService;

    @GetMapping("/listar")
    public ResponseEntity<List<HeroResponse>> listar() {
        return ResponseEntity.ok(heroService.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<HeroResponse> buscarPorId(@PathVariable UUID id) {
        HeroResponse hero = heroService.buscarPorId(id);
        if (hero == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(hero);
        }

    }

    @GetMapping("buscar/{nome}")
    public ResponseEntity<List<HeroResponse>> buscarPorNome(@PathVariable String nome) {
        List<HeroResponse> hero = heroService.buscarPorNome(nome);
        return ResponseEntity.ok(hero);
    }

    @PostMapping
    public ResponseEntity<Void> cadastrar(@Validated
                                          @RequestBody CreateHeroRequest createHeroRequest) {
        final UUID id = heroService.create(createHeroRequest);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<HeroResponse> atualizar(@Validated
                                                  @RequestBody CreateHeroRequest createHeroRequest, @PathVariable UUID id) {

        HeroResponse hero = heroService.editarHero(createHeroRequest, id);
        if (hero == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(hero);
        }
    }
}
