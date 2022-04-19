package candidateStocks;

import dbInterface.DbInterface;
import dbInterface.MongoDBInterface;
import entities.Stock;
import org.bson.Document;

import java.util.List;

public class DBCandidateStocksFetcher implements ICandidateStocksFetcher{
    public static DbInterface<Document> mongoDBInterface;

    static {
        String uri="mongodb+srv://nati:katana@cluster0.ffca4.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
        String dbName="myFirstDatabase";
        String collectionName="CandidateStocks";
        mongoDBInterface = new MongoDBInterface(uri,dbName,collectionName);
    }
    final static String symbol="symbol";
    final static String name="name";
    final static String startPrice="startPrice";
    final static String status="status";
    final static String acquiredDate="acquiredDate";
    final static String cycles="cycles";

    public static Document fromStockToDocument(Stock stock){
        return new Document()
                .append("symbol",stock.getSymbol())
                .append(name,stock.getName())
                .append(startPrice,stock.getStartPrice())
                .append(status,stock.getStatus())
                .append(acquiredDate,stock.getAcquiredDate())
                .append(cycles,stock.getCycles());
    }

    public void createCandidateStock(Stock stock){
        mongoDBInterface.createData(fromStockToDocument(stock));
    }

    @Override
    public List<Stock> getCandidateStocks() {
        return null;
    }

    @Override
    public void UpdateStock(Stock stock) {
        mongoDBInterface.updateData(symbol,fromStockToDocument(stock));
    }

    @Override
    public String printUpdateStockFileActions() {
        return null;
    }
}
