package codes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserNotChangedException extends RuntimeException{
    public UserNotChangedException(String message) {
        super(message);
    }
}
