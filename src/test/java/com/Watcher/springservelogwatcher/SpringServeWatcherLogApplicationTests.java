package com.Watcher.springservelogwatcher;

import candidateStocks.DBCandidateStocksFetcher;
import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entities.Stock;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;


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
					"ACQUIRED","2022-02-19","11");
			dbCandidateStocksFetcher.UpdateStock(stock);
		} catch (Exception e) {
			e.printStackTrace();
		}


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
