package watcher;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtils {

    public static Logger getLogger(String loggerName){
        Logger logger = Logger.getLogger(loggerName);
        String localDir = System.getProperty("user.dir");
//        String loggerPath=localDir + "\\src\\main\\java\\logs\\WatcherLogFile.log";
        String loggerPath=localDir + "\\SpringBootLogWatcher\\src\\main\\resources\\public\\WatcherLogFile.log";
        FileHandler fh;
        SimpleFormatter formatter = new SimpleFormatter();

        try {
            fh = new FileHandler(loggerPath,true);
            logger.addHandler(fh);
            fh.setFormatter(formatter);
            logger.setUseParentHandlers(false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        return logger;
    }
}
