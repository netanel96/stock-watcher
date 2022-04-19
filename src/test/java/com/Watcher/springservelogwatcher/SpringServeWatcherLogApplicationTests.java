package com.Watcher.springservelogwatcher;

import candidateStocks.DBCandidateStocksFetcher;
import candidateStocks.JsonFileCandidateStocksFetcher1;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entities.Stock;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.ArrayEquals;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class SpringServeWatcherLogApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void createStockCandidate(){
		DBCandidateStocksFetcher dbCandidateStocksFetcher=new DBCandidateStocksFetcher();
		Stock stock=new Stock("ABT","Abbott Laboratories",999,
				"ACQUIRED","2022-02-19","17");
		dbCandidateStocksFetcher.createCandidateStock(stock);
	}

	@Test
	void UpdateStockCandidate(){
		try {
			DBCandidateStocksFetcher dbCandidateStocksFetcher=new DBCandidateStocksFetcher();
			Stock stock=new Stock("ABT","Abbott Laboratories",11,
					"ACQUIRED","2022-02-19","133");
			dbCandidateStocksFetcher.UpdateStock(stock);
			System.out.println(dbCandidateStocksFetcher.printUpdateStockFileActions());
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	@Test
	void migrateFromJsonFileToMongoDB(){
		JsonFileCandidateStocksFetcher1 jsonFileCandidateStocksFetcher1 = new JsonFileCandidateStocksFetcher1(System.getProperty("user.dir")+"/src/main//java/configurations/candidateStocks.json");
		DBCandidateStocksFetcher dbCandidateStocksFetcher=new DBCandidateStocksFetcher();

		List<Stock> stockListFromJsonFile= jsonFileCandidateStocksFetcher1.getCandidateStocks();
		if (!(dbCandidateStocksFetcher.getCandidateStocks().isEmpty())){
			System.out.println("cant migrate becuase db is not empty!");
			fail();
		}
		stockListFromJsonFile.forEach(dbCandidateStocksFetcher::createCandidateStock);


		List<Stock> stockListFromMongoDB=dbCandidateStocksFetcher.getCandidateStocks();
//		Assert.assertEquals(stockListFromMongoDB.toArray(), stockListFromJsonFile.toArray());
		assertTrue(stockListFromJsonFile.size() == stockListFromMongoDB.size() && stockListFromJsonFile.containsAll(stockListFromMongoDB) && stockListFromMongoDB.containsAll(stockListFromJsonFile));
	}

	@Test
	void getAllCandidateStocks(){
		DBCandidateStocksFetcher dbCandidateStocksFetcher=new DBCandidateStocksFetcher();
		System.out.println("test res:all stocks:"+dbCandidateStocksFetcher.getCandidateStocks());

	}

	@Test
	void helloWorld() {
		String uri="mongodb+srv://nati:katana@cluster0.ffca4.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";

		try {
//			MongoClient mongoClient = new MongoClient(new MongoClientURI(uri));
//			try {
//				System.out.println("connection to:"+mongoClient.getAddress());
//			} catch (Exception e) {
//				System.out.println("Database unavailable!");
//				mongoClient.close();
//				return;
//			}
			ConnectionString connectionString=new ConnectionString(uri);
			MongoClientSettings settings = MongoClientSettings.builder()
					.applyConnectionString(connectionString)
					.build();
			MongoClient mongoClient = MongoClients.create(settings);
			MongoDatabase database = mongoClient.getDatabase("myFirstDatabase");
//			DB database = mongoClient.getDB("myFirstDatabase");

			database.createCollection("students");
			//Preparing a document
			Document document = new Document();
			document.append("name", "Ram");
			document.append("age", 26);
			document.append("city", "Hyderabad");
			//Inserting the document into the collection
			database.getCollection("students").insertOne(document);
			boolean collectionExists = database.listCollectionNames()
					.into(new ArrayList<String>()).contains("collectionName");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
