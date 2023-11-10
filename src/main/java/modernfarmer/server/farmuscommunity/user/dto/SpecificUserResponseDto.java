package modernfarmer.server.farmuscommunity.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
public class SpecificUserResponseDto {
    private Long id;
    private String imageUrl;
    private String nickName;


}
