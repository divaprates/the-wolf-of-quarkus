package wolf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import wolf.model.ActionIndicatorsModel;

public class ExportCsvService {

    private String HEADER = "Data de extração; Papel; EvEbit; Posição EvEbit, Roic; Posição Roic; Posição Final";
    private String NAMEFILE = "ranking_actions.csv";
    
    public void ExportCsv(List<ActionIndicatorsModel> actionsModelList) throws IOException {

        LocalDate date = LocalDate.now();
        File arquivo = new File("src/main/resources/".concat(NAMEFILE));

        FileWriter writer = new FileWriter(arquivo);
        BufferedWriter buff = new BufferedWriter(writer);

        buff.append(HEADER);

        for (var action : actionsModelList) {
            buff.newLine();
            StringBuilder sb = new StringBuilder();
            sb.append(date).append(";");
            sb.append(action.getPapel()).append(";");
            sb.append(action.getEvEbit()).append(";");
            sb.append(action.getPositionEvEbit()).append(";");
            sb.append(action.getRoic()).append(";");
            sb.append(action.getPositionRoic()).append(";");
            sb.append(action.getPositionFinal()).append(";");
            buff.append(sb.toString());
        }

        buff.close();
        writer.close();
        
    }
}
