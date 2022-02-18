package com.Watcher.springservelogwatcher;

import app.App;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringServeWatcherLogApplication {

	private static void runMainWatcher(String[] args){
		App.main(args);
	}


	public static void main(String[] args) {
		runMainWatcher(args);
		SpringApplication.run(SpringServeWatcherLogApplication.class, args);
	}

}
