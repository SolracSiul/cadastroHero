package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class PowerStatsRepository {

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
            " (strength, agility, dexterity, intelligence)" +
            " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String UPDATE_POWER_STATS_QUERY = "UPDATE power_stats"
            + " SET strength = :strength, agility = :agility, dexterity = :dexterity, intelligence = :intelligence, updated_at = :updatedAt"
            +  " WHERE id = :id RETURNING *";

    private static final String GET_ID_POWER_STATS_QUERY = "SELECT * FROM power_stats where id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(PowerStats powerStats) {
        return namedParameterJdbcTemplate.queryForObject(
                CREATE_POWER_STATS_QUERY,
                new BeanPropertySqlParameterSource(powerStats),
                UUID.class);
    }


    PowerStats buscarPorId(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(GET_ID_POWER_STATS_QUERY, params,new PowerStatsRowMapper());
    };


    PowerStats atualizar(PowerStats powerStats) {
        final Map<String, Object> params = Map.of("id", powerStats.getId(),
                "strength", powerStats.getStrength(),
                "agility", powerStats.getAgility(),
                "dexterity",powerStats.getAgility(),
                "intelligence",powerStats.getIntelligence(),
                "updatedAt", Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        return namedParameterJdbcTemplate.queryForObject(UPDATE_POWER_STATS_QUERY, params,new PowerStatsRowMapper());
    };


}
