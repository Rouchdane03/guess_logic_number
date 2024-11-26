package codes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassementController {

    private static int COUNTER = 0;
    record Rank(String result){};

    @GetMapping("/rank")
    public Rank getRank(){
        return new Rank("Rank: %s".formatted(++COUNTER));
    }
}
