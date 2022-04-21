package com.Watcher.springservelogwatcher;

import app.Watcher;
import candidateStocks.DBCandidateStocksFetcher;

import java.io.FileNotFoundException;
import java.util.TimerTask;

public class WatcherScheduledTask  extends TimerTask {
    @Override
    public void run() {
        try {
            Watcher watcher = new Watcher();
            System.out.println("watching..");
            watcher.watch();
            DBCandidateStocksFetcher.mongoDBInterface.close();

        } catch (FileNotFoundException e) {
            System.out.println("some sheet happened");
            e.printStackTrace();
        }
    }
}
