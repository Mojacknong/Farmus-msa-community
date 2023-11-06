package modernfarmer.server.farmuscommunity.community.dto.request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WriteCommentRequest {

    @NotNull(message = "null 값을 가지면 안됩니다.")
    private Long postingId;


    @NotNull(message = "null 값을 가지면 안됩니다.")
    private String comment;
}
