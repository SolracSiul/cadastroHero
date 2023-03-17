package br.com.gubee.interview.model.request;

import br.com.gubee.interview.model.PowerStats;
import lombok.Data;

import java.util.UUID;

@Data
public class HeroLutas {
    private UUID hero1;
    private UUID hero2;

    private String maiorAgilidade;

    private String maiorInteligencia;
    private String maiorForca;
    private String maiorDestreza;

    private String ganhador;


}
