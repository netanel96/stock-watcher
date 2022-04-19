package com.Watcher.springservelogwatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
public class WatcherController {

    @GetMapping("/")
    public String index() {

        return "<div><button type=\"button\">Click Me!</button></div>";
    }
    private String getLogData() {
        InputStream inputStream = WatcherController.class.getResourceAsStream("\\public\\WatcherLogFile.log");
        String data = null;
        try {
            data = readFromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
