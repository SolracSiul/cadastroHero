package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    @Transactional
    public UUID create(PowerStats powerStats) {
        return powerStatsRepository.create(powerStats);
    }



    public PowerStats buscarPorId(UUID id){
        return powerStatsRepository.buscarPorId(id);

    }
    public PowerStats atualizar(PowerStats powerStats){
        return powerStatsRepository.atualizar(powerStats);

    }
}



