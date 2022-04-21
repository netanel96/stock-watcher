package com.Watcher.springservelogwatcher;

import app.App;
import app.LoggerUtils;
import app.Watcher;
import candidateStocks.DBCandidateStocksFetcher;
import entities.Stock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

import java.util.Timer;
import java.util.logging.Logger;


@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class SpringServeWatcherLogApplication {
	static Logger logger = LoggerUtils.getLogger(SpringServeWatcherLogApplication.class.getName());

	private static void runMainWatcher(String[] args){
		try {
			logger.info("updating in MongoDb...");
			DBCandidateStocksFetcher dbCandidateStocksFetcher = new DBCandidateStocksFetcher();
			Stock stock = new Stock("ABT", "Abbott Laboratories", 999,
					"ACQUIRED", "2022-04-19", "123");
			dbCandidateStocksFetcher.UpdateStock(stock);
			logger.info("updated a stock in MongoDb.");
			System.out.println("db connection is good.");

			Timer time = new Timer();
			WatcherScheduledTask st = new WatcherScheduledTask();
			time.schedule(st, 0, 1000*2400);

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
