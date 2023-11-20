package modernfarmer.server.farmuscommunity.community.dto.response;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import modernfarmer.server.farmuscommunity.global.exception.success.SuccessMessage;
import modernfarmer.server.farmuscommunity.global.exception.fail.ErrorMessage;



@Getter
@Builder
@JsonPropertyOrder({"code", "message", "data"})
public class BaseResponseDto<T> {
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> BaseResponseDto<T> of(SuccessMessage successMessage, T data){

        return BaseResponseDto.<T>builder()
                .code(successMessage.getCode())
                .message(successMessage.getMessage())
                .data(data)
                .build();
    }

    public static <T> BaseResponseDto<T> of(ErrorMessage errorMessage){

        return BaseResponseDto.<T>builder()
                .code(errorMessage.getCode())
                .message(errorMessage.getMessage())
                .build();
    }


}