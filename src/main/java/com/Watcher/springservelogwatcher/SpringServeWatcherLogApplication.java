package com.Watcher.springservelogwatcher;

import app.App;
import app.Watcher;
import candidateStocks.DBCandidateStocksFetcher;
import entities.Stock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;


@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class SpringServeWatcherLogApplication {

	private static void runMainWatcher(String[] args){
		try {
			Watcher watcher = new Watcher();
			System.out.println("watching..");
			System.out.println("updating MongoDb..");
			DBCandidateStocksFetcher dbCandidateStocksFetcher=new DBCandidateStocksFetcher();
			Stock stock=new Stock("ABT","Abbott Laboratories",100,
					"ACQUIRED","2022-04-19","123");
			dbCandidateStocksFetcher.UpdateStock(stock);
			watcher.watch();
			DBCandidateStocksFetcher.mongoDBInterface.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR, some shit happend.");
		}
	}


	public static void main(String[] args) {
		runMainWatcher(args);
		SpringApplication.run(SpringServeWatcherLogApplication.class, args);
	}

}
