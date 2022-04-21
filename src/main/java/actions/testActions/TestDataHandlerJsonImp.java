package actions.testActions;

import candidateStocks.AbstractJsonArrayIO;
import entities.Stock;
import entities.TestData;
import investProps.InvestProps;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TestDataHandlerJsonImp extends AbstractJsonArrayIO<TestData> implements ITestDataHandler {
    private static String valueInPercentage = "valueInPercentage";
    private static String symbolHistory = "symbolHistory";

    public TestDataHandlerJsonImp(String path) {
        super(path);
    }

    public TestData getTestData(){
        return jsonObjectList.get(0);
    }

    @Override
    public void updateTestData(Stock stock, InvestProps investProps) {
        double oldPercentageValue = jsonObjectList.get(0).getValueInPercentage();
        double updatedPercentageValue = investProps.getPercentageEntry() * 100 + oldPercentageValue;
        String oldSymbolHistory = jsonObjectList.get(0).getSymbolHistory();
        String updatedSymbolHistory = oldSymbolHistory+TestData.getSymbolHistoryDelimiter()+stock.getSymbol();
        this.UpdateObject(new TestData(updatedPercentageValue, updatedSymbolHistory));
    }

    @Override
    protected void addToList(JSONObject jsonObject) {
        jsonObjectList.add(new TestData(
                Double.parseDouble((String) jsonObject.get(valueInPercentage)),
                (String) jsonObject.get(symbolHistory)));
    }

    @Override
    protected TestData getObjectFromList(TestData objectIdentifier) {
        return jsonObjectList.get(0);
    }

    @Override
    protected void updateJsonArray(JSONArray array, TestData object) {
        for (Object o : array) {
            JSONObject itemArr = (JSONObject) o;
            itemArr.put(valueInPercentage, String.valueOf(object.getValueInPercentage()));
            itemArr.put(symbolHistory, String.valueOf(object.getSymbolHistory()));
        }
    }
}
