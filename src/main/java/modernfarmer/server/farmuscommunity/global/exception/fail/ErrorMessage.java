package modernfarmer.server.farmuscommunity.global.exception.fail;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorMessage {

    INTERVAL_SERVER_ERROR(2001,  "요청을 처리하는 과정에서 서버가 예상하지 못한 오류가 발생하였습니다."),
    REFRESH_NOTIFICATION_ERROR(2002,  "Refresh Token 인증 오류"),
    OBJECT_URL_INSERT_ERROR (2003,"객체 URL을 처리하는 과정에서 오류가 났습니다."),
    NOT_EXIST_COMMENT (2004,"댓글이 존재하지 않습니다.");



    private final int code;
    private final String message;

    ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}