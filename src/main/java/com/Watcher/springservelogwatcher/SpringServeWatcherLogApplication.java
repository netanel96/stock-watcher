package com.Watcher.springservelogwatcher;

import app.App;
import app.Watcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringServeWatcherLogApplication {

	private static void runMainWatcher(String[] args){
		try {
			Watcher watcher = new Watcher();
			System.out.println("watching..");
			watcher.watch();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR, some shit happend");
		}
	}


	public static void main(String[] args) {
		runMainWatcher(args);
		SpringApplication.run(SpringServeWatcherLogApplication.class, args);
	}

}
