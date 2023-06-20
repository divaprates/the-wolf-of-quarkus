package wolf.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import wolf.dto.ActionIndicatorsDTO;

public class Aplications {
 
    public static void main(String[] args) throws IOException{
        
        Document doc = Jsoup.connect("https://www.fundamentus.com.br/resultado.php").get();
        Element table = doc.select("table#resultado").first();

        boolean header = true;
        List<ActionIndicatorsDTO> actionIndicatorList = new ArrayList<>();

        for(Element row : table.select("tr")) {
            if(header){
                header = false;
                continue;
            }
        
            Elements column = row.select("td");
            ActionIndicatorsDTO dto = new ActionIndicatorsDTO();
            
            dto.setPapel(column.get(0).select("a").text());
            dto.setCotacao(column.get(1).text());
            dto.setPl(column.get(2).text());
            dto.setEvEbit(column.get(10).text());
            dto.setRoic(column.get(15).text());
            dto.setLiq2meses(column.get(17).text());

            actionIndicatorList.add(dto);
        }
        
    }
    
}
