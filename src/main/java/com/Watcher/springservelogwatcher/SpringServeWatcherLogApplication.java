package com.Watcher.springservelogwatcher;

import app.App;
import app.Watcher;
import candidateStocks.DBCandidateStocksFetcher;
import entities.Stock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringServeWatcherLogApplication {

	private static void runMainWatcher(String[] args){
		try {
			Watcher watcher = new Watcher();
			System.out.println("watching..");
			System.out.println("updating MongoDb..");
			DBCandidateStocksFetcher dbCandidateStocksFetcher=new DBCandidateStocksFetcher();
			Stock stock=new Stock("ABT","Abbott Laboratories",100,
					"ACQUIRED","2022-02-19","100");
			dbCandidateStocksFetcher.UpdateStock(stock);
			watcher.watch();

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
