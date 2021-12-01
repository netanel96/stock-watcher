package com.Watcher.springservelogwatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WatcherController {

    @GetMapping("/")
    public String index() {
        return "Welcome to watcher stock on Heroku deo !!";
    }
}
