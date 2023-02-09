package shop.mtcoding.blog.handler.ex;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomApiException extends RuntimeException {

    private HttpStatus status;

    public CustomApiException(String msg, HttpStatus status) { // 상태코드 입력 받음
        super(msg);
        this.status = status;
    }

    public CustomApiException(String msg) { // 400 상테코드를 입력하지 않았을때 적용
        this(msg, HttpStatus.BAD_REQUEST);
    }
}
