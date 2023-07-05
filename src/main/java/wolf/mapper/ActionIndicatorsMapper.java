package wolf.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import wolf.dto.ActionIndicatorsDTO;
import wolf.model.ActionIndicatorsModel;

public class ActionIndicatorsMapper {

    public ActionIndicatorsModel toModel(ActionIndicatorsDTO dto) {
        ActionIndicatorsModel model = new ActionIndicatorsModel();

        model.setPapel(dto.getPapel());
        model.setCotacao(convertStringToBigDecimal(dto.getCotacao()));
        model.setPl(convertStringToBigDecimal(dto.getPl()));
        model.setEvEbit(convertStringToBigDecimal(dto.getEvEbit()));
        model.setRoic(convertStringToBigDecimalPercentage(dto.getRoic()));
        model.setLiq2meses(convertStringToBigDecimal(dto.getLiq2meses()));

        return model;

    }

    private BigDecimal convertStringToBigDecimal(String value) {
        BigDecimal bd;
        try {
            bd = new BigDecimal(value.replace("%", "")).setScale(2);
        } catch (Exception e) {
            bd = null;
        }

        return bd;
    }

    private BigDecimal convertStringToBigDecimalPercentage(String value) {
        BigDecimal bd;
        try {
            bd = new BigDecimal(value.replace("%", "")).setScale(4).divide(new BigDecimal("100"), RoundingMode.HALF_UP);
        } catch (Exception e) {
            bd = null;
        }

        return bd;
    }

}
