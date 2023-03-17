package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import br.com.gubee.interview.model.request.HeroResponse;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest) {
        PowerStats powerStats = new PowerStats(createHeroRequest);
        UUID powerStatsID = powerStatsService.create(powerStats);
        Hero hero = new Hero(createHeroRequest,powerStatsID);
        return heroRepository.create(hero);
    }



    public List<HeroResponse> listar() {
        List<Hero> heroes = heroRepository.listarTudo();
        List<HeroResponse> response = new ArrayList<>();
        for (Hero hero: heroes) {
            HeroResponse heroResponse = new HeroResponse();
            PowerStats powerStats = powerStatsService.buscarPorId(hero.getPowerStatsId());
            heroResponse.setHero(hero);
            heroResponse.setPowerStats(powerStats);
            response.add(heroResponse);
        }
        return response;
    }

    public HeroResponse buscarPorId(UUID id){
        Hero hero = heroRepository.buscarPorId(id);
        if(hero == null){
            return null;
        }
        HeroResponse heroResponse = new HeroResponse();
        PowerStats powerStats = powerStatsService.buscarPorId(hero.getPowerStatsId());
        heroResponse.setHero(hero);
        heroResponse.setPowerStats(powerStats);

        return heroResponse;
    }

    public List<HeroResponse> buscarPorNome(String nome){
        List<Hero> heroes = heroRepository.buscarPorNome(nome);
        List<HeroResponse> response = new ArrayList<>();
        for (Hero hero: heroes) {
            HeroResponse heroResponse = new HeroResponse();
            PowerStats powerStats = powerStatsService.buscarPorId(hero.getPowerStatsId());
            heroResponse.setHero(hero);
            heroResponse.setPowerStats(powerStats);
            response.add(heroResponse);
        }
        return response;
    }

    @Transactional
    public HeroResponse editarHero(CreateHeroRequest createHeroRequest,UUID heroID) {
        Hero hero = heroRepository.buscarPorId(heroID);
        if(hero == null){
            return null;
        }
        PowerStats powerStats = new PowerStats(createHeroRequest);
        powerStats.setId(hero.getPowerStatsId());
        PowerStats powerStatsNovo = powerStatsService.atualizar(powerStats);

        Hero heroNovo = heroRepository.atualizar(createHeroRequest,heroID);

        HeroResponse heroResponse = new HeroResponse();
        heroResponse.setPowerStats(powerStatsNovo);
        heroResponse.setHero(heroNovo);

        return heroResponse;
    }
}
