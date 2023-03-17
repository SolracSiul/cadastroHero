package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.enums.Race;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HeroRowMapper implements RowMapper<Hero> {
    public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {
        Hero hero = new Hero(
                (UUID) rs.getObject("id"),
                rs.getString("name"),
                Race.stringParaEnum(rs.getString("race")),
                (UUID) rs.getObject("power_stats_id"),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant(),
                rs.getBoolean("enabled")
        );
        return hero;
    }
}