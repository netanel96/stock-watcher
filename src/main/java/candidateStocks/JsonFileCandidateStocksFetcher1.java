package candidateStocks;

import entities.Stock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class JsonFileCandidateStocksFetcher1 extends AbstractJsonArrayIO<Stock> implements  ICandidateStocksFetcher{
    String candidateStocksPath=System.getProperty("user.dir") + "\\src\\main\\java\\configurations\\candidateStocks.json";
//    String candidateStocksPath="C:\\MyProjects\\Watch\\candidateStocks.json";
    final static String symbol="symbol";
    final static String name="name";
    final static String startPrice="startPrice";
    final static String status="status";
    final static String acquiredDate="acquiredDate";
    final static String cycles="cycles";

    public JsonFileCandidateStocksFetcher1(String path) {
        super(path);
    }

    @Override
    protected void addToList(JSONObject jsonObject) {
        jsonObjectList.add(new Stock((String) jsonObject.get(symbol),
                (String) jsonObject.get(name),
                Double.parseDouble((String)jsonObject.get(startPrice)),
                (String)jsonObject.get(status),
                (String)jsonObject.get(acquiredDate),
                (String)jsonObject.get(cycles)
        ));
    }

    @Override
    protected Stock getObjectFromList(Stock objectIdentifier) {
        return jsonObjectList.stream().filter
                (myStock->myStock.getSymbol().equals(objectIdentifier.getSymbol())).
                collect(Collectors.toList()).get(0);
    }

    @Override
    protected void updateJsonArray(JSONArray array, Stock object) {
        for (Object o : array) {
            JSONObject itemArr = (JSONObject) o;
            if (itemArr.get(symbol).equals(object.getSymbol())) {
                itemArr.put(cycles, String.valueOf(Integer.parseInt(object.getCycles())));
                itemArr.put(status, object.getStatus());
                itemArr.put(acquiredDate, object.getAcquiredDate());
            }
        }
    }

    @Override
    public List<Stock> getCandidateStocks() {
        return jsonObjectList;
    }

    @Override
    public void UpdateStock(Stock stock) {
        UpdateObject(stock);
    }

    @Override
    public String printUpdateStockFileActions() {
        return UpdateJsonFileLog;
    }
}
