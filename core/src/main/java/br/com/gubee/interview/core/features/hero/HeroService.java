package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.HeroLutas;
import br.com.gubee.interview.model.request.HeroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    private final PowerStatsService powerStatsService;

    private int GANHADOR1 = 0;
    private int GANHADOR2 = 0;

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest) {
        PowerStats powerStats = new PowerStats(createHeroRequest);
        UUID powerStatsID = powerStatsService.create(powerStats);
        Hero hero = new Hero(createHeroRequest, powerStatsID);
        return heroRepository.create(hero);
    }


    public List<HeroResponse> listar() {
        List<Hero> heroes = heroRepository.listarTudo();
        List<HeroResponse> response = new ArrayList<>();
        for (Hero hero : heroes) {
            HeroResponse heroResponse = new HeroResponse();
            PowerStats powerStats = powerStatsService.buscarPorId(hero.getPowerStatsId());
            heroResponse.setHero(hero);
            heroResponse.setPowerStats(powerStats);
            response.add(heroResponse);
        }
        return response;
    }

    public HeroResponse buscarPorId(UUID id) {
        Hero hero = heroRepository.buscarPorId(id);
        if (hero == null) {
            return null;
        }
        HeroResponse heroResponse = new HeroResponse();
        PowerStats powerStats = powerStatsService.buscarPorId(hero.getPowerStatsId());
        heroResponse.setHero(hero);
        heroResponse.setPowerStats(powerStats);

        return heroResponse;
    }

    public List<HeroResponse> buscarPorNome(String nome) {
        List<Hero> heroes = heroRepository.buscarPorNome(nome);
        List<HeroResponse> response = new ArrayList<>();
        for (Hero hero : heroes) {
            HeroResponse heroResponse = new HeroResponse();
            PowerStats powerStats = powerStatsService.buscarPorId(hero.getPowerStatsId());
            heroResponse.setHero(hero);
            heroResponse.setPowerStats(powerStats);
            response.add(heroResponse);
        }
        return response;
    }

    @Transactional
    public HeroResponse editarHero(CreateHeroRequest createHeroRequest, UUID heroID) {
        Hero hero = heroRepository.buscarPorId(heroID);
        if (hero == null) {
            return null;
        }
        PowerStats powerStats = new PowerStats(createHeroRequest);
        powerStats.setId(hero.getPowerStatsId());
        PowerStats powerStatsNovo = powerStatsService.atualizar(powerStats);

        Hero heroNovo = heroRepository.atualizar(createHeroRequest, heroID);

        HeroResponse heroResponse = new HeroResponse();
        heroResponse.setPowerStats(powerStatsNovo);
        heroResponse.setHero(heroNovo);

        return heroResponse;
    }


    public void deleteHeroById(UUID id) throws ChangeSetPersister.NotFoundException {
        try {
            Hero heroDelete = heroRepository.buscarPorId(id);
            if (heroDelete == null) {
                throw new ChangeSetPersister.NotFoundException();
            } else {
                heroRepository.deleteHeroById(id);
            }
        } catch (Exception e) {
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    public HeroLutas comparar(UUID heroCod, UUID hero2Cod) {
        HeroResponse hero1 = buscarPorId(heroCod);
        HeroResponse hero2 = buscarPorId(hero2Cod);

        if (hero1 == null || hero2 == null) {
            return null;
        }
        HeroLutas heroLutas = new HeroLutas();

        heroLutas.setHero1(hero1.getHero().getId());
        heroLutas.setHero2(hero2.getHero().getId());

        heroLutas.setMaiorForca(compararForca(hero1, hero2, GANHADOR1, GANHADOR2));
        heroLutas.setMaiorDestreza(compararDestreza(hero1, hero2, GANHADOR1, GANHADOR2));
        heroLutas.setMaiorAgilidade(compararAgilidade(hero1, hero2, GANHADOR1, GANHADOR2));
        heroLutas.setMaiorInteligencia(compararInteligencia(hero1, hero2, GANHADOR1, GANHADOR2));

        if (GANHADOR1 > GANHADOR2) {
            heroLutas.setGanhador(hero1.getHero().getName());
        } else if (GANHADOR2 > GANHADOR1) {
            heroLutas.setGanhador(hero2.getHero().getName());
        } else {
            heroLutas.setGanhador("Os dois jogadores são iguais");
        }

        zerarVitorias();
        return heroLutas;

    }

    public String compararAgilidade(HeroResponse hero1, HeroResponse hero2, int GANHADOR1, int GANHADOR2) {
        if (hero1.getPowerStats().getAgility() > hero2.getPowerStats().getAgility()) {
           addVitoriaGanhador1();
            String mensagem = "O ganhador entre " + hero1.getHero().getName() + " e " + hero2.getHero().getName() + " é: " + hero1.getHero().getName();
            return mensagem;
        } else if (hero2.getPowerStats().getAgility() < hero1.getPowerStats().getAgility()) {
            addVitoriaGanhador2();
            String mensagem = "O ganhador entre " + hero1.getHero().getName() + " e " + hero2.getHero().getName() + " é: " + hero2.getHero().getName();
            return mensagem;
        } else {
            String mensagem = "Empate";
            return mensagem;
        }
    }

    public String compararInteligencia(HeroResponse hero1, HeroResponse hero2, int GANHADOR1, int GANHADOR2) {
        if (hero1.getPowerStats().getIntelligence() > hero2.getPowerStats().getIntelligence()) {
            addVitoriaGanhador1();
            String mensagem = "O ganhador entre " + hero1.getHero().getName() + " e " + hero2.getHero().getName() + " é: " + hero1.getHero().getName();
            return mensagem;
        } else if (hero2.getPowerStats().getIntelligence() < hero1.getPowerStats().getIntelligence()) {
            addVitoriaGanhador2();
            String mensagem = "O ganhador entre " + hero1.getHero().getName() + " e " + hero2.getHero().getName() + " é: " + hero2.getHero().getName();
            return mensagem;
        } else {
            String mensagem = "Empate";
            return mensagem;
        }
    }

    public String compararForca(HeroResponse hero1, HeroResponse hero2, int GANHADOR1, int GANHADOR2) {
        if (hero1.getPowerStats().getStrength() > hero2.getPowerStats().getStrength()) {
            addVitoriaGanhador1();
            String mensagem = "O ganhador entre " + hero1.getHero().getName() + " e " + hero2.getHero().getName() + " é: " + hero1.getHero().getName();
            return mensagem;
        } else if (hero2.getPowerStats().getStrength() < hero1.getPowerStats().getStrength()) {
            addVitoriaGanhador2();
            String mensagem = "O ganhador entre " + hero1.getHero().getName() + " e " + hero2.getHero().getName() + " é: " + hero2.getHero().getName();
            return mensagem;
        } else {
            String mensagem = "Empate";
            return mensagem;
        }
    }

    public String compararDestreza(HeroResponse hero1, HeroResponse hero2, int GANHADOR1, int GANHADOR2) {
        if (hero1.getPowerStats().getDexterity() > hero2.getPowerStats().getDexterity()) {
            addVitoriaGanhador1();
            String mensagem = "O ganhador entre " + hero1.getHero().getName() + " e " + hero2.getHero().getName() + " é: " + hero1.getHero().getName();
            return mensagem;
        } else if (hero2.getPowerStats().getDexterity() < hero1.getPowerStats().getDexterity()) {
            addVitoriaGanhador2();
            String mensagem = "O ganhador entre " + hero1.getHero().getName() + " e " + hero2.getHero().getName() + " é: " + hero2.getHero().getName();
            return mensagem;
        } else {
            String mensagem = "Empate";
            return mensagem;
        }
    }


    public void addVitoriaGanhador1() {
        this.GANHADOR1 = GANHADOR1 + 1;
    }


    public void addVitoriaGanhador2() {
        this.GANHADOR2 = GANHADOR2 + 1;
    }

    public void zerarVitorias(){
        this.GANHADOR2 = 0;
        this.GANHADOR1 = 0;
    }

}
