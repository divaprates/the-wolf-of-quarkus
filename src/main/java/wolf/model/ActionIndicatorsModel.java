package wolf.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ActionIndicatorsModel {

    private String papel;
    private BigDecimal cotacao;
    private BigDecimal pl;
    private BigDecimal evEbit;
    private BigDecimal roic;
    private BigDecimal liq2meses;

    private int positionEvEbit;
    private int positionRoic;
    private int positionFinal;
    
}
