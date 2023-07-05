package wolf.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import wolf.dto.ActionIndicatorsDTO;
import wolf.mapper.ActionIndicatorsMapper;
import wolf.model.ActionIndicatorsModel;

public class ExtractionService {

    public List<ActionIndicatorsModel> extract() throws IOException {
        Document doc = Jsoup.connect("https://www.fundamentus.com.br/resultado.php").get();
        Element table = doc.select("table#resultado").first();

        boolean header = true;
        List<ActionIndicatorsDTO> actionIndicatorList = new ArrayList<>();

        for (Element row : table.select("tr")) {
            if (header) {
                header = false;
                continue;
            }

            Elements column = row.select("td");
            ActionIndicatorsDTO dto = new ActionIndicatorsDTO();

            dto.setPapel(clearExtractedText(column.get(0).select("a").text()));
            dto.setCotacao(clearExtractedText(column.get(1).text()));
            dto.setPl(clearExtractedText(column.get(2).text()));
            dto.setEvEbit(clearExtractedText(column.get(10).text()));
            dto.setRoic(clearExtractedText(column.get(15).text()));
            dto.setLiq2meses(clearExtractedText(column.get(17).text()));

            actionIndicatorList.add(dto);
        }

        ActionIndicatorsMapper mapper = new ActionIndicatorsMapper();

        List<ActionIndicatorsModel> actionsModelList = actionIndicatorList.stream()
                .map(a -> mapper.toModel(a)).collect(Collectors.toList());

        return actionsModelList;
    }

    public Set<String> extract(String setor) throws IOException {
        Document doc = Jsoup.connect("https://www.fundamentus.com.br/resultado.php?setor=".concat(setor)).get();
        Element table = doc.select("table#resultado").first();

        boolean header = true;
        List<ActionIndicatorsDTO> actionIndicatorList = new ArrayList<>();

        for (Element row : table.select("tr")) {
            if (header) {
                header = false;
                continue;
            }

            Elements column = row.select("td");
            ActionIndicatorsDTO dto = new ActionIndicatorsDTO();

            dto.setPapel(clearExtractedText(column.get(0).select("a").text()));
            actionIndicatorList.add(dto);
        }
        return actionIndicatorList.stream()
                .map(a -> a.getPapel()).collect(Collectors.toSet());
    }

    private static String clearExtractedText(String text) {
        return text.replace(".", "").replace(",", ".");
    }
}
