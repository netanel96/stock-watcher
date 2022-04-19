package dbInterface;

import app.LoggerUtils;
import com.Watcher.springservelogwatcher.SpringServeWatcherLogApplication;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class MongoDBInterface implements DbInterface<Document> {
    //    private static MongoDBInterface mongoDBInterface;
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static String  collectionName;
    Logger logger = LoggerUtils.getLogger(SpringServeWatcherLogApplication.class.getName());
    public MongoDBInterface(String uri, String dbName,String collectionName) {
        try {
            if (mongoClient == null) {
                initDB(uri);
                database = mongoClient.getDatabase(dbName);
                this.collectionName=collectionName;
                boolean collectionExists = database.listCollectionNames()
                        .into(new ArrayList<String>()).contains(collectionName);
                if (collectionExists==false){
                    database.createCollection(collectionName);
                }
                logger.info("\n\nConnected to MongoDB!!\n");
            }

        } catch (Exception e) {
            System.out.println("couldn't connect to mongoDB.");
            e.printStackTrace();
        }
    }

    @Override
    public void initDB(String uri) {
        try {

            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
        } catch (Exception e) {
            System.out.println("Couldn't connect to mongo db\n");
            e.printStackTrace();
        }
    }

    @Override
    public void createData(Document data) {
        database.getCollection(collectionName).insertOne(data);
    }

    @Override
    public Document readData(String id) {
        return null;
    }

    @Override
    public List<Document> readListOfData(Document document) {
        return null;
    }

    @Override
    public List<Document> readListOfData() {
        List<Document> res=new ArrayList<>();
        database.getCollection(collectionName).find().iterator().forEachRemaining(res::add);
        return  res;
    }

    @Override
    public void updateData(String id , Document data) {
        Bson query = eq(id, data.get(id));
        database.getCollection(collectionName).replaceOne(query,data);
    }

    @Override
    public void deleteData(String id) {

    }

    @Override
    public void close() {
        mongoClient.close();
    }
}
