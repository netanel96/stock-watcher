package candidateStocks;

import entities.Stock;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonFileCandidateStocksFetcher implements  ICandidateStocksFetcher{
    String localDir = System.getProperty("user.dir");
//    String candidateStocksPath=localDir + "\\src\\main\\java\\configurations\\candidateStocks.json";
    String candidateStocksPath="C:\\MyProjects\\Watch\\candidateStocks.json";
    JSONParser jsonParser = new JSONParser();
    List<Stock> stockList=new ArrayList<>();
    String UpdateCandidatesFileLog="";
    final static String symbol="symbol";
    final static String name="name";
    final static String startPrice="startPrice";
    final static String status="status";
    final static String acquiredDate="acquiredDate";
    final static String cycles="cycles";

    public JsonFileCandidateStocksFetcher() {
        JSONArray jsonArray = initiateJsonArray();
        setStockList(jsonArray);
    }

    private void setStockList(JSONArray jsonArray) {
        stockList=new ArrayList<>();
        jsonArray.forEach(jsonObject->addToStockList((JSONObject)jsonObject));
    }

    private void addToStockList(JSONObject jsonObject) {
        stockList.add(new Stock((String) jsonObject.get(symbol),
                (String) jsonObject.get(name),
                Double.parseDouble((String)jsonObject.get(startPrice)),
                (String)jsonObject.get(status),
                (String)jsonObject.get(acquiredDate),
                (String)jsonObject.get(cycles)
        ));
    }

    @Override
    public List<Stock> getCandidateStocks() {
        return stockList;
    }

    @Override
    public void UpdateStock(Stock updateStock) {
        JSONArray array = initiateJsonArray();
        updateJsonArray(array,updateStock);
        writeJsonArrayToFile(array);
        logUpdateOfCandidateFile(getStockFromList(updateStock.getSymbol()),updateStock);
        setStockList(array);
        }

    private void logUpdateOfCandidateFile(Stock oldStock, Stock updateStock) {
        UpdateCandidatesFileLog+="\n"+"old Stock:"+oldStock+"\n"+"update Stock:"+updateStock+"\n";
    }

    @Override
    public String printUpdateStockFileActions() {
        return UpdateCandidatesFileLog;
    }

    public Stock getStockFromList(String symbol){
        return stockList.stream().filter
                (myStock->myStock.getSymbol().equals(symbol)).
                collect(Collectors.toList()).get(0);
    }

    private JSONArray initiateJsonArray() {
        JSONArray array = null;
        try {
            array = (JSONArray) jsonParser.parse(new FileReader(candidateStocksPath));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return array;
    }

    private void writeJsonArrayToFile(JSONArray array) {
        try (FileWriter file = new FileWriter(candidateStocksPath)) {
            file.write(array.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateJsonArray(JSONArray array,Stock stock) {
        for (Object o : array) {
            JSONObject itemArr = (JSONObject) o;
            if (itemArr.get(symbol).equals(stock.getSymbol())) {
                itemArr.put(cycles, String.valueOf(Integer.parseInt(stock.getCycles())));
                itemArr.put(status, stock.getStatus());
            }
        }
    }
}
