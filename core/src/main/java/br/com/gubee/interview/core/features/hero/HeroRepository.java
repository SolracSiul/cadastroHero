package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.Timestamp;
import java.time.LocalDate;
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

    private static final String GET_ID_LISTAR_HERO_QUERY_QUERY = "SELECT * FROM hero where id = :id";

    private static final String GET_BUSCA_HERO_NOME_QUERY_QUERY = "SELECT * FROM hero WHERE name LIKE :nameBuscado";

    private static final String UPDATE_HERO_QUERY = "UPDATE hero"
            + " SET name = :name, race = :race, updated_at = :updatedAt"
            +  " WHERE id = :id RETURNING *";

    private static final String DELETE_HERO_BY_ID = "DELETE FROM hero WHERE id = :id";

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
                LISTAR_HERO_QUERY,new HeroRowMapper());
    }


    Hero buscarPorId(UUID id) {
       try {
           final Map<String, Object> params = Map.of("id", id);
           return namedParameterJdbcTemplate.queryForObject(GET_ID_LISTAR_HERO_QUERY_QUERY, params,new HeroRowMapper());
       }catch (Exception e){
           return null;
       }
    };

    List<Hero> buscarPorNome(String nome) {
        final Map<String, Object> params = Map.of("nameBuscado", "%" + nome + "%");
        return namedParameterJdbcTemplate.query(GET_BUSCA_HERO_NOME_QUERY_QUERY, params,new HeroRowMapper());

    };

    Hero atualizar(CreateHeroRequest hero,UUID id) {
        final Map<String, Object> params = Map.of("id", id,
                "name", hero.getName(),
                "race", hero.getRace().name(),
                "updatedAt", Timestamp.valueOf(LocalDate.now().atStartOfDay()));
        return namedParameterJdbcTemplate.queryForObject(UPDATE_HERO_QUERY, params,new HeroRowMapper());
    };

    public void deleteHeroById(UUID id) throws  ChangeSetPersister.NotFoundException {
            try {
                final Map<String, Object> params = Map.of("id", id);
                namedParameterJdbcTemplate.update(DELETE_HERO_BY_ID, params);
            } catch (Exception e) {
                throw new ChangeSetPersister.NotFoundException();
            }
        }
    }


