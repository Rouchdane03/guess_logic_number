package codes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidStarsException extends RuntimeException{
    public InvalidStarsException(String message){
        super(message);
    }
}
