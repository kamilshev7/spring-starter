package guard.passer.core.http.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(basePackages = "guard.passer.core.http.rest")
public class RestExceptionHandlerController extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception exception){
//        log.error("Failed to return response", exception);
//        return "error/error500";
//    }
}
