package codes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static codes.GuessLogicNumberApplication.GENERATED_VALUE;

@RestController
@RequestMapping(path = "api/v1/number")
public class NumberController {

    @GetMapping("/{number}")
    public GuessNumber guessTheNumber(@PathVariable int number) {

        Clue clue = Clue.NO_NEED_ALREADY_FOUND;

        if(number!=GENERATED_VALUE){
            return new GuessNumber(number,false,clue.giveMeClue(GENERATED_VALUE));
        }
        else return new GuessNumber(number,true,clue);
    }
    record GuessNumber(int inputValue, boolean guessed, Clue clue){}
}
