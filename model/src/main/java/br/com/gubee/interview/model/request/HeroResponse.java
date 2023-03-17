package br.com.gubee.interview.model.request;


import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import lombok.Data;

@Data
public class HeroResponse {

    public Hero hero;

    public PowerStats powerStats;
}
