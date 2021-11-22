package com.Watcher.springservelogwatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import watcher.App;

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
