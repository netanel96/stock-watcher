package actions.testActions;

import dbInterface.DbInterface;
import dbInterface.MongoDBInterface;
import entities.Stock;
import entities.TestData;
import investProps.InvestProps;
import org.bson.Document;

public class DBTestDataHandler implements ITestDataHandler {
    static String collectionName = "TestData";
    public static DbInterface<Document> mongoDBInterface= MongoDBInterface.getInstance().selectCollection(collectionName);

    private static String stringId="name";
    private static String valueInPercentage = "valueInPercentage";
    private static String symbolHistory = "symbolHistory";

    public static Document fromTestDataToDocument(TestData testData){
        return new Document().append(stringId,"TestData")
                .append(valueInPercentage,testData.getValueInPercentage())
                .append(symbolHistory,testData.getSymbolHistory());
    }

    public static TestData fromDocumentToTestData(Document document){
        return new TestData(Double.parseDouble(document.get(valueInPercentage).toString()),
                document.get(symbolHistory).toString());
    }

    @Override
    public void updateTestData(Stock stock, InvestProps investProps) {
        TestData oldTestData=fromDocumentToTestData(MongoDBInterface.getInstance().selectCollection(collectionName).readData(""));
        double oldPercentageValue = oldTestData.getValueInPercentage();
        double updatedPercentageValue = investProps.getPercentageEntry() * 100 + oldPercentageValue;
        String oldSymbolHistory = oldTestData.getSymbolHistory();
        String updatedSymbolHistory = oldSymbolHistory+ TestData.getSymbolHistoryDelimiter()+stock.getSymbol();
        MongoDBInterface.getInstance().selectCollection(collectionName).updateData(stringId,fromTestDataToDocument(new TestData(updatedPercentageValue, updatedSymbolHistory)));
    }

    public void createTestData(TestData testData){
        MongoDBInterface.getInstance().selectCollection(collectionName).createData(fromTestDataToDocument(testData));
    }
}
