package candidateStocks;

import dbInterface.DbInterface;
import dbInterface.MongoDBInterface;
import entities.Stock;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

//TODO refactor it to work with DAO pattern.
public class DBCandidateStocksFetcher implements ICandidateStocksFetcher {
    static String collectionName = "CandidateStocks";
    public static DbInterface<Document> mongoDBInterface=MongoDBInterface.getInstance().selectCollection(collectionName);

//    static {
//        String uri = "mongodb+srv://nati:katana@cluster0.ffca4.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
//        String dbName = "myFirstDatabase";
//        String collectionName = "CandidateStocks";
//        mongoDBInterface = new MongoDBInterface(uri, dbName, collectionName);
//    }

    final static String symbol = "symbol";
    final static String name = "name";
    final static String startPrice = "startPrice";
    final static String status = "status";
    final static String acquiredDate = "acquiredDate";
    final static String cycles = "cycles";
    private String updatesLog="";

    public static Document fromStockToDocument(Stock stock) {
        return new Document()
                .append(symbol, stock.getSymbol())
                .append(name, stock.getName())
                .append(startPrice, stock.getStartPrice())
                .append(status, stock.getStatus())
                .append(acquiredDate, stock.getAcquiredDate())
                .append(cycles, stock.getCycles());
    }

    public static Stock fromDocumentToStock(Document document) {
        Stock stock=null;
        try {
            stock = new Stock(
                    document.get(symbol).toString(),
                    document.get(name).toString(),
                    Double.parseDouble(document.get(startPrice).toString()),

                    document.get(status).toString(),
                    document.get(acquiredDate).toString(),
                    document.get(cycles).toString());
        } catch (NumberFormatException e) {
            System.out.println("could not convert document to stock Object.\ndocument:"+document.toString());
            e.printStackTrace();
        }
        return stock;
    }

    public void createCandidateStock(Stock stock) {
        MongoDBInterface.getInstance().selectCollection(collectionName).createData(fromStockToDocument(stock));
    }

    @Override
    public List<Stock> getCandidateStocks() {
        List<Stock> stockList = new ArrayList<>();
        MongoDBInterface.getInstance().selectCollection(collectionName).readListOfData().forEach(doc -> {
            stockList.add(fromDocumentToStock(doc));
        });

        //VALIDATE THERE ARE NO DUPLICATES OF SAME SYMBOL.
        Optional<Stock> optionalStock= stockList.stream().filter
                (stock -> Collections.frequency(
                        stockList.stream().map(Stock::getSymbol).collect(Collectors.toList()),
                        stock.getSymbol()) > 1)
                .findFirst();
        if (optionalStock.isPresent()){
            System.out.println("WARNING STOCK LIST CONTAINS DUPLICATE STOCK SYMBOL! Please Fix this.");
            System.out.println("duplicated symbol is:" + optionalStock.get().getSymbol());

        }
        return stockList;
    }

    @Override
    public void UpdateStock(Stock stock) {
        Stock oldStock=getCandidateStocks().stream().filter(prevStock->prevStock.getSymbol().equals(stock.getSymbol())).findFirst().get();
        MongoDBInterface.getInstance().selectCollection(collectionName).updateData(symbol, fromStockToDocument(stock));
        updatesLog +="\n"+"old stock:"+oldStock+"\n"+"updated stock:"+stock+"\n";

    }

    @Override
    public String printUpdateStockFileActions() {
        return updatesLog;
    }
}
