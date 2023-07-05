package wolf.app;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import wolf.model.ActionIndicatorsModel;
import wolf.service.ExportCsvService;
import wolf.service.ExtractionService;

public class Aplications {

    public static void main(String[] args) throws IOException {

        ExtractionService extractionService = new ExtractionService();
        ExportCsvService exportCsvServiceService = new ExportCsvService();

        List<ActionIndicatorsModel> actionsModelList = extractionService.extract();

        // remover ações com menos de 100 mil negociados em 2 meses
        actionsModelList = actionsModelList.stream()
                .filter(a -> a.getLiq2meses().compareTo(new BigDecimal("100000")) >= 0).collect(Collectors.toList());

        actionsModelList = actionsModelList.stream()
                .filter(a -> a.getEvEbit().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList());

        // remover seguradoras, bancos e empresas de energia
        // id seguros = 31
        Set<String> acoesSeguros = extractionService.extract("31");
        // id energia = 14
        Set<String> acoesEnergia = extractionService.extract("14");
        // id bancos = 20
        Set<String> acoesBanco = extractionService.extract("20");

        Set<String> exclusion = acoesSeguros;
        exclusion.addAll(acoesEnergia);
        exclusion.addAll(acoesBanco);

        actionsModelList = actionsModelList.stream()
                .filter(a -> !exclusion.contains(a.getPapel())).collect(Collectors.toList());
        // filtrar ações repetidas
        Map<String, ActionIndicatorsModel> rolesRepeated = new HashMap<>();

        for (var action : actionsModelList) {
            String paper = action.getPapel().replaceAll("[0-9]", "");
            if (rolesRepeated.keySet().contains(paper)) {
                if (action.getLiq2meses().compareTo(rolesRepeated.get(paper).getLiq2meses()) >= 0) {
                    rolesRepeated.put(paper, action);
                }
            } else {
                rolesRepeated.put(paper, action);
            }
        }
        actionsModelList = rolesRepeated.values().stream().collect(Collectors.toList());
        
        int sizeList = actionsModelList.size();
        
        actionsModelList.sort(new Comparator<ActionIndicatorsModel>() {
            @Override
            public int compare(ActionIndicatorsModel a1, ActionIndicatorsModel a2) {
                return a1.getEvEbit().compareTo(a2.getEvEbit());
            }
        });
        for (int i = 0; i < sizeList; i++) {
            actionsModelList.get(i).setPositionEvEbit(i+1);
        }

        actionsModelList.sort(new Comparator<ActionIndicatorsModel>() {
            @Override
            public int compare(ActionIndicatorsModel a1, ActionIndicatorsModel a2) {
                return a2.getRoic().compareTo(a1.getRoic());
            }
        });
        for (int i = 0; i < sizeList; i++) {
            actionsModelList.get(i).setPositionRoic(i+1);
            actionsModelList.get(i).setPositionFinal(actionsModelList.get(i).getPositionEvEbit() + actionsModelList.get(i).getPositionRoic());
        }

        actionsModelList.sort(new Comparator<ActionIndicatorsModel>() {
            @Override
            public int compare(ActionIndicatorsModel a1, ActionIndicatorsModel a2) {
                return a1.getPositionFinal() - a2.getPositionFinal();
            }
        });
        for (int i = 0; i < sizeList; i++) {
            actionsModelList.get(i).setPositionFinal(i+1);
        }

        exportCsvServiceService.ExportCsv(actionsModelList);

        /*for (var action : actionsModelList) {
            System.out.println(action);
        }*/

    }
}
