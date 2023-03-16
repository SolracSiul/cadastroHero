package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id)" +
            " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String LISTAR_HERO_QUERY = "SELECT * from hero";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(Hero hero) {
        final Map<String, Object> params = Map.of("name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
                CREATE_HERO_QUERY,
                params,
                UUID.class);
    }


    List<Hero> listarTudo() {
        return namedParameterJdbcTemplate.query(
                LISTAR_HERO_QUERY,
                (obj, linha) ->
                        new Hero(
                                (UUID) obj.getObject("id"),
                                obj.getString("name"),
                                null,
                                (UUID) obj.getObject("powerStatsId"),
                                obj.getTimestamp("createdAt").toInstant(),
                                obj.getTimestamp("updatedAt").toInstant(),
                                obj.getBoolean("enabled")
                                ));
    }
}


